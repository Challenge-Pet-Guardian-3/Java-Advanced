package fiap.com.br.petguardian.usuario.dto;

import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6)
        String senha,

        @NotBlank(message = "Telefone e obrigatorio")
        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 ou 11 digitos, somente numeros")
        String telefone,

        @NotBlank(message = "CEP e obrigatorio")
        @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 digitos, somente numeros")
        String cep
) {
    public Usuario toEntity() {
        return Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .telefone(telefone)
                .build();
    }
}