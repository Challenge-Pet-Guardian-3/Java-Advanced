package fiap.com.br.petguardian.atendimento.dto;

import fiap.com.br.petguardian.atendimento.Atendimento;
import fiap.com.br.petguardian.atendimento.tipoatendimento.EnumTipoAtendimento;
import fiap.com.br.petguardian.atendimento.tipoatendimento.TipoAtendimento;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.status.EnumStatus;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.validation.EnumValidation;
import fiap.com.br.petguardian.veterinario.Veterinario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record AtendimentoRequest(
        @NotNull
        @EnumValidation(enumClass = EnumTipoAtendimento.class)
        String tipo,

        @NotNull
        @FutureOrPresent(message = "Prazo não pode estar no passado.")
        LocalDateTime data,

        @NotBlank
        @Size(max = 300)
        String anotacoes,

        @NotNull
        @PositiveOrZero
        Double valor,

        @NotBlank
        @EnumValidation(enumClass = EnumStatus.class)
        String status,

        @NotNull
        Long petId,

        @NotNull
        Long veterinarioId
) {
    public Atendimento toEntity(TipoAtendimento tipoObj, Pet pet, Veterinario veterinario, Status status) {
        return Atendimento.builder()
                .tipo(tipoObj)
                .data(data)
                .anotacoes(anotacoes)
                .valor(valor)
                .status(status)
                .pet(pet)
                .veterinario(veterinario)
                .build();
    }
}
