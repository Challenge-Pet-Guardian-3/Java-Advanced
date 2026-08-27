package fiap.com.br.petguardian.usuariopet.dto;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import fiap.com.br.petguardian.usuariopet.UsuarioPetId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CoCuidadorRequest(
        @NotNull(message = "O ID do responsavel principal e obrigatorio")
        @Schema(description = "ID do tutor responsavel principal que esta autorizando o convite", example = "1")
        Long responsavelPrincipalId,

        @NotBlank(message = "O e-mail do co-cuidador e obrigatorio")
        @Email(message = "Formato de e-mail invalido")
        @Schema(description = "E-mail do usuario a ser convidado como co-cuidador", example = "cuidador@petguardian.com")
        String email
) {
    public UsuarioPet toEntity(Usuario convidado, Pet pet) {
        return UsuarioPet.builder()
                .id(new UsuarioPetId(convidado.getId(), pet.getId()))
                .usuario(convidado)
                .pet(pet)
                .responsavelPrincipal(false)
                .build();
    }
}
