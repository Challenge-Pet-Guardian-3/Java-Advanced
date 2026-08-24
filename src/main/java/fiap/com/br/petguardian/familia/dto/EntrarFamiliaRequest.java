package fiap.com.br.petguardian.familia.dto;

import jakarta.validation.constraints.NotBlank;

public record EntrarFamiliaRequest(@NotBlank String codigo, String funcao) {}