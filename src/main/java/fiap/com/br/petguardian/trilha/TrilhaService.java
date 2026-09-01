package fiap.com.br.petguardian.trilha;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.familia.XpService;
import fiap.com.br.petguardian.trilha.dto.TrilhaConclusaoRequest;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrilhaService {

    private final TrilhaEtapaConcluidaRepository trilhaEtapaConcluidaRepository;
    private final UsuarioRepository usuarioRepository;
    private final XpService xpService;

    public List<TrilhaEtapaConcluida> listarConcluidas(Long usuarioId) {
        findUsuarioById(usuarioId);
        return trilhaEtapaConcluidaRepository.findAllByUsuarioId(usuarioId);
    }

    // Idempotente: se a etapa já foi concluída antes, retorna o registro existente
    // sem conceder XP de novo (trava anti-farm — mesma regra que já existia no front).
    @Transactional
    public TrilhaEtapaConcluida concluirEtapa(Long usuarioId, TrilhaConclusaoRequest request) {
        Usuario usuario = findUsuarioById(usuarioId);

        var existente = trilhaEtapaConcluidaRepository.findByUsuarioIdAndEtapaId(usuario.getId(), request.etapaId());
        if (existente.isPresent()) {
            return existente.get();
        }

        int xp = request.xp() != null ? request.xp() : 0;

        TrilhaEtapaConcluida etapa = TrilhaEtapaConcluida.builder()
                .usuario(usuario)
                .etapaId(request.etapaId())
                .tipo(request.tipo())
                .xpGanho(xp)
                .dataConclusao(LocalDateTime.now())
                .build();

        TrilhaEtapaConcluida salva = trilhaEtapaConcluidaRepository.save(etapa);

        xpService.adicionar(usuario.getId(), xp);

        return salva;
    }

    public Integer calcularPontosTotaisUsuario(Long usuarioId) {
        findUsuarioById(usuarioId);
        return trilhaEtapaConcluidaRepository.somarXpByUsuarioId(usuarioId);
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com id " + id + " não encontrado."));
    }
}