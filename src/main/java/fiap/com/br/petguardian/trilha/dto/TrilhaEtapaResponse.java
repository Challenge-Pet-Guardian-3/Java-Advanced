package fiap.com.br.petguardian.trilha.dto;

import fiap.com.br.petguardian.trilha.TrilhaEtapaConcluida;

import java.time.LocalDateTime;

public record TrilhaEtapaResponse(
        Long id,
        String etapaId,
        String tipo,
        Integer xpGanho,
        LocalDateTime dataConclusao
) {
    public static TrilhaEtapaResponse fromEntity(TrilhaEtapaConcluida etapa) {
        return new TrilhaEtapaResponse(
                etapa.getId(),
                etapa.getEtapaId(),
                etapa.getTipo(),
                etapa.getXpGanho(),
                etapa.getDataConclusao()
        );
    }
}