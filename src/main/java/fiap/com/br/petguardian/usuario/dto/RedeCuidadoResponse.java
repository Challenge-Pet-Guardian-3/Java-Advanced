package fiap.com.br.petguardian.usuario.dto;

import java.util.List;

public record RedeCuidadoResponse(
        Long usuarioId,
        String nomeUsuario,
        List<PetResumo> pets,
        List<CuidadorResumo> coCuidadores,
        int totalTarefasPendentes,
        int totalTarefasConcluidas,
        int totalAtendimentos,
        int pontosAcumulados
) {
    public record PetResumo(
            Long id,
            String nome,
            String raca,
            boolean responsavelPrincipal,
            List<Long> tarefaIds,
            List<Long> atendimentoIds
    ) {}

    public record CuidadorResumo(
            Long id,
            String nome,
            String email,
            boolean responsavelPrincipal,
            List<Long> petIds
    ) {}
}
