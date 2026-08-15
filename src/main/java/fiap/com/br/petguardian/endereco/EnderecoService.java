package fiap.com.br.petguardian.endereco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fiap.com.br.petguardian.endereco.bairro.Bairro;
import fiap.com.br.petguardian.endereco.bairro.BairroRepository;
import fiap.com.br.petguardian.endereco.cidade.Cidade;
import fiap.com.br.petguardian.endereco.cidade.CidadeRepository;
import fiap.com.br.petguardian.endereco.dto.EnderecoRequest;
import fiap.com.br.petguardian.endereco.estado.Estado;
import fiap.com.br.petguardian.endereco.estado.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private static final String VIACEP_ENDPOINT = "https://viacep.com.br/ws/{cep}/json/";

    private final EnderecoRepository enderecoRepository;
    private final EstadoRepository estadoRepository;
    private final CidadeRepository cidadeRepository;
    private final BairroRepository bairroRepository;
    private final RestClient restClient = RestClient.create();

    public Page<Endereco> findAll(Pageable pageable) {
        return enderecoRepository.findAll(pageable);
    }

    public Endereco findById(Long id) {
        return findEnderecoById(id);
    }

    public Endereco create(EnderecoRequest enderecoRequest) {
        return findOrCreateByCepAndNumero(enderecoRequest);
    }

    public Endereco update(Long id, EnderecoRequest enderecoRequest) {
        findEnderecoById(id);
        Endereco endereco = buildEnderecoFromCep(enderecoRequest);
        endereco.setId(id);
        return enderecoRepository.save(endereco);
    }

    public void delete(Long id) {
        findEnderecoById(id);
        enderecoRepository.deleteById(id);
    }

    public Endereco findOrCreateByCepAndNumero(EnderecoRequest enderecoRequest) {
        String cleanCep = enderecoRequest.cep().replaceAll("\\D", "");
        ResolvedAddress resolvedAddress = resolveAddressFromCep(cleanCep);
        return enderecoRepository.findByCepAndNumeroAndBairroId(cleanCep, enderecoRequest.numero(), resolvedAddress.bairro().getId())
                .orElseGet(() -> enderecoRepository.save(enderecoRequest.toEntity(resolvedAddress.rua(), resolvedAddress.bairro())));
    }

    private Endereco findEnderecoById(Long id) {
        return enderecoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço com id " + id + " não encontrado."));
    }

    private Endereco buildEnderecoFromCep(EnderecoRequest enderecoRequest) {
        String cleanCep = enderecoRequest.cep().replaceAll("\\D", "");
        ResolvedAddress resolvedAddress = resolveAddressFromCep(cleanCep);
        return enderecoRequest.toEntity(resolvedAddress.rua(), resolvedAddress.bairro());
    }

    private ResolvedAddress resolveAddressFromCep(String cep) {
        try {
            var response = restClient.get()
                    .uri(VIACEP_ENDPOINT, cep)
                    .retrieve()
                    .body(ViaCepResponse.class);

            if (response == null || Boolean.TRUE.equals(response.erro())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP " + cep + " não encontrado.");

            Estado estado = findOrCreateEstado(response.estado());
            Cidade cidade = findOrCreateCidade(response.localidade(), estado);
            Bairro bairro = findOrCreateBairro(response.bairro(), cidade);

            return new ResolvedAddress(response.logradouro(), bairro);
        } catch (RestClientException error) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao consultar o serviço de CEP.", error);
        }
    }

    private Estado findOrCreateEstado(String nomeEstado) {
        return estadoRepository.findByNomeIgnoreCase(nomeEstado)
                .orElseGet(() -> estadoRepository.save(Estado.builder().nome(nomeEstado).build()));
    }

    private Cidade findOrCreateCidade(String nomeCidade, Estado estado) {
        return cidadeRepository.findByNomeIgnoreCaseAndEstadoId(nomeCidade, estado.getId())
                .orElseGet(() -> cidadeRepository.save(Cidade.builder().nome(nomeCidade).estado(estado).build()));
    }

    private Bairro findOrCreateBairro(String nomeBairro, Cidade cidade) {
        return bairroRepository.findByNomeIgnoreCaseAndCidadeId(nomeBairro, cidade.getId())
                .orElseGet(() -> bairroRepository.save(Bairro.builder().nome(nomeBairro).cidade(cidade).build()));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ViaCepResponse(
            String logradouro,
            String bairro,
            String localidade,
            String estado,
            Boolean erro
    ) {}

    private record ResolvedAddress(String rua, Bairro bairro) {}
}
