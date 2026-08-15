package fiap.com.br.petguardian.clinica.dto;

import fiap.com.br.petguardian.clinica.Clinica;
import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.dto.EnderecoRequest;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.validation.DddValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClinicaRequest(
        @NotBlank
        String nome,

        @NotBlank
        @DddValidation
        String ddd,

        @NotBlank
        @Pattern(regexp = "\\d{9}", message = "Número de telefone deve conter exatamente 9 dígitos numéricos.")
        String numeroTelefone,

        @NotNull
        @Valid
        EnderecoRequest endereco
) {
    public Clinica toEntity(Telefone telefone, Endereco endereco) {
        return Clinica.builder()
                .nome(nome)
                .telefone(telefone)
                .endereco(endereco)
                .build();
    }
}
