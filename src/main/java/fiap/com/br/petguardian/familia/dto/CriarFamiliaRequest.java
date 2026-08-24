package fiap.com.br.petguardian.familia.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarFamiliaRequest(@NotBlank String nome) {}