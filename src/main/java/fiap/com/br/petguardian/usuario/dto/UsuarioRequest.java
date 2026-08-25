package fiap.com.br.petguardian.usuario.dto;

import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 6)
        String senha
) {
    public Usuario toEntity() {
        return Usuario.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .build();
    }
}