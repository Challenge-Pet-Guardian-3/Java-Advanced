package fiap.com.br.petguardian.atendimento.dto;

import fiap.com.br.petguardian.atendimento.Atendimento;

import java.time.LocalDateTime;

public record AtendimentoResponse(
        Long id,
        String tipo,
        LocalDateTime data,
        String anotacoes,
        String status,
        Double valor,
        Long petId,
        Long veterinarioId
) {
    public static AtendimentoResponse fromEntity(Atendimento atendimento) {
        return new AtendimentoResponse(
                atendimento.getId(),
                atendimento.getTipo().getTipoAtendimento().name(),
                atendimento.getData(),
                atendimento.getAnotacoes(),
                atendimento.getStatus().getNome_status().name(),
                atendimento.getValor(),
                atendimento.getPet().getId(),
                atendimento.getVeterinario().getId()
        );
    }
}
