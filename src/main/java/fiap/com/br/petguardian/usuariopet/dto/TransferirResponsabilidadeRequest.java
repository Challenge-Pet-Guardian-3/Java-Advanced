package fiap.com.br.petguardian.usuariopet.dto;

import fiap.com.br.petguardian.validation.DiferentesUsuariosValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@DiferentesUsuariosValidation
public record TransferirResponsabilidadeRequest(
        @NotNull(message = "O ID do responsavel atual e obrigatorio")
        @Schema(description = "ID do tutor que atualmente e o responsavel principal", example = "1")
        Long responsavelAtualId,

        @NotNull(message = "O ID do novo responsavel e obrigatorio")
        @Schema(description = "ID do usuario co-cuidador que passara a ser o responsavel principal", example = "2")
        Long novoResponsavelId
) {}
