package fiap.com.br.petguardian.trilha.dto;

import jakarta.validation.constraints.NotBlank;

public record TrilhaConclusaoRequest(
        @NotBlank String etapaId,
        @NotBlank String tipo,
        Integer xp
) {}