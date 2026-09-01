package fiap.com.br.petguardian.pet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetPorte;
import fiap.com.br.petguardian.pet.raca.Raca;
import fiap.com.br.petguardian.validation.EnumValidation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

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
        Long usuarioId,

        @Positive
        Double peso,

        @PastOrPresent
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate ultimaVacina,

        @PastOrPresent
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate ultimaConsulta,

        Integer avatarId
) {
        public Pet toEntity(Raca racaObj) {
                return Pet.builder()
                        .nome(nome)
                        .idade(idade)
                        .raca(racaObj)
                        .porte(PetPorte.valueOf(porte.toUpperCase()))
                        .sexo(sexo)
                        .castrado(castrado)
                        .peso(peso)
                        .ultimaVacina(ultimaVacina)
                        .ultimaConsulta(ultimaConsulta)
                        .avatarId(avatarId)
                        .build();
        }
}