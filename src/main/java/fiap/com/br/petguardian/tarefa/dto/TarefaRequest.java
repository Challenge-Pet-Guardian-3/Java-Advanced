package fiap.com.br.petguardian.tarefa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.status.EnumStatus;
import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.veterinario.Veterinario;
import fiap.com.br.petguardian.validation.EnumValidation;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TarefaRequest(
        @NotBlank
        String titulo,

        @NotNull
        @Positive(message = "Pontos da tarefa devem ser maiores que zero.")
        Integer pontosTarefa,

        @NotBlank
        String descricao,

        @NotNull
        @FutureOrPresent(message = "Prazo não pode estar no passado.")
        LocalDateTime prazo,

        Long usuarioId,

        @NotNull
        Long petId,

        @NotBlank
        @EnumValidation(enumClass = EnumStatus.class)
        String status,

        @NotNull
        Long veterinarioId
) {
    public Tarefa toEntity(Usuario usuario, Pet pet, Veterinario veterinario) {
        return Tarefa.builder()
                .titulo(titulo)
                .pontosTarefa(pontosTarefa)
                .descricao(descricao)
                .criacao(LocalDateTime.now())
                .prazo(prazo)
                .usuario(usuario)
                .pet(pet)
                .veterinario(veterinario)
                .build();
    }
}
