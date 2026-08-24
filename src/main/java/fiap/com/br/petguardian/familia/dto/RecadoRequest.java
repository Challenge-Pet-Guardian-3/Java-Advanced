package fiap.com.br.petguardian.familia.dto;

import jakarta.validation.constraints.NotBlank;

public record RecadoRequest(@NotBlank String texto) {}