package fiap.com.br.petguardian.familia;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Centraliza o ajuste de XP do membro da família, usado tanto por conclusão/
// reabertura/exclusão de tarefas quanto por conclusão de etapas da trilha.
// Evita repetir o mesmo bloco de "buscar membro -> somar/subtrair -> salvar"
// em vários services.
@Service
@RequiredArgsConstructor
public class XpService {

    private final FamiliaMembroRepository familiaMembroRepository;

    public void adicionar(Long usuarioId, int pontos) {
        ajustar(usuarioId, pontos);
    }

    public void remover(Long usuarioId, int pontos) {
        ajustar(usuarioId, -pontos);
    }

    private void ajustar(Long usuarioId, int delta) {
        familiaMembroRepository.findByUsuarioId(usuarioId).ifPresent(membro -> {
            int xpAtual = membro.getXp() != null ? membro.getXp() : 0;
            membro.setXp(Math.max(0, xpAtual + delta));
            familiaMembroRepository.save(membro);
        });
    }
}