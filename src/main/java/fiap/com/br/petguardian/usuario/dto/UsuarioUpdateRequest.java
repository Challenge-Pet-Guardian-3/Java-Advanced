package fiap.com.br.petguardian.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        // Opcional: se vier nulo/vazio, a senha atual é preservada.
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String senha
) {}