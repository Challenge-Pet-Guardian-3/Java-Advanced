package fiap.com.br.petguardian.tarefa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.status.EnumStatus;
import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.validation.EnumValidation;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TarefaRequest(
        @NotBlank(message = "O título é obrigatório.")
        String titulo,

        @NotNull(message = "Os pontos da tarefa são obrigatórios.")
        @Positive(message = "Pontos da tarefa devem ser maiores que zero.")
        Integer pontosTarefa,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "O prazo é obrigatório.")
        @FutureOrPresent(message = "Prazo não pode estar no passado.")
        LocalDateTime prazo,

        Long usuarioId,

        @NotNull(message = "O ID do pet é obrigatório.")
        Long petId,

        @NotBlank(message = "O status é obrigatório.")
        @EnumValidation(enumClass = EnumStatus.class)
        String status
) {
    public Tarefa toEntity(Usuario usuario, Pet pet) {
        return Tarefa.builder()
                .titulo(titulo)
                .pontosTarefa(pontosTarefa)
                .descricao(descricao)
                .criacao(LocalDateTime.now())
                .prazo(prazo)
                .usuario(usuario)
                .pet(pet)
                .build();
    }
}