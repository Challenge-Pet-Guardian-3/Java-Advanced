package fiap.com.br.petguardian.endereco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoRequest(
        @NotBlank
        String logradouro,

        String numero,

        String complemento,

        String bairro,

        @NotBlank
        String cidade,

        @NotBlank
        @Size(min = 2, max = 2, message = "UF deve ter 2 letras.")
        String estado,

        @NotBlank
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido.")
        String cep
) {}