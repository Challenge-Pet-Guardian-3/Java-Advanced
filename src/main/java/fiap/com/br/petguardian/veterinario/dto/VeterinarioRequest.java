package fiap.com.br.petguardian.veterinario.dto;

import fiap.com.br.petguardian.clinica.Clinica;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.veterinario.Veterinario;
import fiap.com.br.petguardian.validation.DddValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VeterinarioRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6)
        String senha,

        @NotBlank
        @DddValidation
        String ddd,

        @NotBlank
        @Pattern(regexp = "\\d{9}", message = "Número de telefone deve conter exatamente 9 dígitos numéricos.")
        String numeroTelefone,

        Long clinicaId
) {
    public Veterinario toEntity(Telefone telefone, Clinica clinica) {
        return Veterinario.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .telefone(telefone)
                .clinica(clinica)
                .build();
    }
}
