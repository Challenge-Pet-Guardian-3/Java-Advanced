package fiap.com.br.petguardian.atendimento.tipoatendimento;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class TipoAtendimentoService {
    private final TipoAtendimentoRepository tipoAtendimentoRepository;

    public Page<TipoAtendimento> findAll(Pageable pageable) {
        return tipoAtendimentoRepository.findAll(pageable);
    }

    public TipoAtendimento findById(Long id) {
        return findTipoAtendimentoById(id);
    }

    @Cacheable(value = "tipoAtendimento", key = "#nome")
    public TipoAtendimento findTipoAtendimentoByNome(String nome) {
        EnumTipoAtendimento enumTipo = parseEnumTipoAtendimento(nome);
        return tipoAtendimentoRepository.findByTipoAtendimento(enumTipo).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de atendimento '" + nome + "' não encontrado."));
    }

    private TipoAtendimento findTipoAtendimentoById(Long id) {
        return tipoAtendimentoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de atendimento com id " + id + " não encontrado."));
    }

    private EnumTipoAtendimento parseEnumTipoAtendimento(String nome) {
        try {
            return EnumTipoAtendimento.valueOf(nome.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de atendimento inválido: " + nome);
        }
    }
}
