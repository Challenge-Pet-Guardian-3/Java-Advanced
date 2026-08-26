package fiap.com.br.petguardian.usuario;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.EnderecoService;
import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.telefone.TelefoneRepository;
import fiap.com.br.petguardian.usuario.dto.RedeCuidadoResponse;
import fiap.com.br.petguardian.usuario.dto.RedeCuidadoResponse.CuidadorResumo;
import fiap.com.br.petguardian.usuario.dto.RedeCuidadoResponse.PetResumo;
import fiap.com.br.petguardian.usuario.dto.UsuarioRequest;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final EnderecoService enderecoService;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioPetRepository usuarioPetRepository;
    private final TarefaRepository tarefaRepository;

    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Page<Usuario> findByNome(String nome, Pageable pageable) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Usuario findById(Long id) {
        return findUsuarioById(id);
    }

    @Transactional
    public Usuario create(UsuarioRequest usuarioRequest) {
        return salvarUsuarioComRelacionamentos(null, usuarioRequest);
    }

    @Transactional
    public Usuario update(Long id, UsuarioRequest usuarioRequest) {
        findUsuarioById(id);
        return salvarUsuarioComRelacionamentos(id, usuarioRequest);
    }

    public void delete(Long id) {
        findUsuarioById(id);
        usuarioRepository.deleteById(id);
    }

    public RedeCuidadoResponse getRedeCuidado(Long usuarioId) {
        Usuario usuario = findUsuarioById(usuarioId);
        List<UsuarioPet> meusVinculos = usuarioPetRepository.findAllByUsuarioId(usuarioId);
        List<Long> petIds = meusVinculos.stream().map(up -> up.getPet().getId()).toList();

        if (petIds.isEmpty()) {
            return new RedeCuidadoResponse(usuarioId, usuario.getNome(), List.of(), List.of(), 0, 0, 0);
        }

        List<PetResumo> petResumos = montarPetResumos(meusVinculos);
        List<CuidadorResumo> coCuidadores = montarCoCuidadores(petIds, usuarioId);

        int totalPendentes = tarefaRepository.countPendentesByPetIdIn(petIds);
        int totalConcluidas = tarefaRepository.countConcluidasByPetIdIn(petIds);
        int pontos = tarefaRepository.calcularPontosTotaisUsuario(usuarioId);

        return new RedeCuidadoResponse(
                usuarioId,
                usuario.getNome(),
                petResumos,
                coCuidadores,
                totalPendentes,
                totalConcluidas,
                pontos
        );
    }

    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com email " + email + " nao encontrado."));
    }

    private Usuario salvarUsuarioComRelacionamentos(Long id, UsuarioRequest request) {
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(request.endereco());
        Telefone telefone = telefoneRepository.save(Telefone.builder()
                .ddd(request.ddd())
                .numero(request.numeroTelefone())
                .build());

        Usuario usuario = request.toEntity(telefone);
        usuario.setId(id);
        usuario.getEnderecos().add(endereco);
        return usuarioRepository.save(usuario);
    }

    private List<PetResumo> montarPetResumos(List<UsuarioPet> vinculos) {
        return vinculos.stream()
                .map(vinculo -> {
                    Long petId = vinculo.getPet().getId();
                    List<Long> tarefaIds = tarefaRepository.findIdsByPetId(petId);
                    return new PetResumo(
                            petId,
                            vinculo.getPet().getNome(),
                            vinculo.getPet().getRaca().getNome(),
                            vinculo.isResponsavelPrincipal(),
                            tarefaIds
                    );
                })
                .toList();
    }

    private List<CuidadorResumo> montarCoCuidadores(List<Long> petIds, Long usuarioIdAtual) {
        return usuarioPetRepository.findAllByPetIdIn(petIds).stream()
                .filter(vinculo -> !vinculo.getUsuario().getId().equals(usuarioIdAtual))
                .collect(Collectors.groupingBy(UsuarioPet::getUsuario))
                .entrySet().stream()
                .map(entry -> {
                    Usuario cuidador = entry.getKey();
                    List<UsuarioPet> vinculos = entry.getValue();
                    boolean responsavelPrincipal = vinculos.stream()
                            .anyMatch(UsuarioPet::isResponsavelPrincipal);
                    List<Long> petIdsDoCuidador = vinculos.stream()
                            .map(v -> v.getPet().getId())
                            .toList();

                    return new CuidadorResumo(
                            cuidador.getId(),
                            cuidador.getNome(),
                            cuidador.getEmail(),
                            responsavelPrincipal,
                            petIdsDoCuidador
                    );
                })
                .toList();
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario com id " + id + " nao encontrado."));
    }
}
