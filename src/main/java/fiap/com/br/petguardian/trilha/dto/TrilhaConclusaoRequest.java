package fiap.com.br.petguardian.trilha.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrilhaConclusaoRequest(
        @NotNull Long usuarioId,
        @NotBlank String etapaId,
        @NotBlank String tipo,
        Integer xp
) {}