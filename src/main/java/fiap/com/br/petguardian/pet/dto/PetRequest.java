package fiap.com.br.petguardian.pet.dto;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetPorte;
import fiap.com.br.petguardian.pet.raca.Raca;
import fiap.com.br.petguardian.validation.EnumValidation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetRequest(
        @NotBlank
        String nome,

        @NotNull
        @Min(0)
        Integer idade,

        @NotBlank
        String raca,

        @NotNull
        @EnumValidation(enumClass = PetPorte.class)
        String porte,

        @NotNull
        Character sexo,

        @NotNull
        Boolean castrado,

        @NotNull
        Long usuarioId
) {
    public Pet toEntity(Raca racaObj) {
        return Pet.builder()
                .nome(nome)
                .idade(idade)
                .raca(racaObj)
                .porte(PetPorte.valueOf(porte.toUpperCase()))
                .sexo(sexo)
                .castrado(castrado)
                .build();
    }
}
