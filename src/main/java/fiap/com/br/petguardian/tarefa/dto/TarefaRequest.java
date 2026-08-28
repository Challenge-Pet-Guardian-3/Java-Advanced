package fiap.com.br.petguardian.tarefa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.tarefa.status.EnumStatus;
import fiap.com.br.petguardian.tarefa.status.Status;
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

        @EnumValidation(enumClass = EnumStatus.class)
        String status
) {
    public EnumStatus statusEnum() {
        return (status != null && !status.isBlank()) ? EnumStatus.valueOf(status.trim().toUpperCase()) : EnumStatus.PENDENTE;
    }

    public Tarefa toEntity(Pet pet, Status statusEntity, LocalDateTime criacao) {
        return Tarefa.builder()
                .titulo(titulo)
                .pontosTarefa(pontosTarefa)
                .descricao(descricao)
                .criacao(criacao)
                .prazo(prazo)
                .status(statusEntity)
                .pet(pet)
                .build();
    }

    public Tarefa toEntity(Usuario usuario, Pet pet, Status statusEntity, LocalDateTime criacao) {
        return Tarefa.builder()
                .titulo(titulo)
                .pontosTarefa(pontosTarefa)
                .descricao(descricao)
                .criacao(criacao)
                .prazo(prazo)
                .status(statusEntity)
                .usuario(usuario)
                .pet(pet)
                .build();
    }

    public Tarefa aplicarEm(Tarefa tarefa, Pet pet, Usuario usuario, Status statusEntity, LocalDateTime conclusao) {
        tarefa.setTitulo(titulo);
        tarefa.setPontosTarefa(pontosTarefa);
        tarefa.setDescricao(descricao);
        tarefa.setPrazo(prazo);
        tarefa.setPet(pet);
        tarefa.setUsuario(usuario);
        tarefa.setStatus(statusEntity);
        tarefa.setConclusao(conclusao);
        return tarefa;
    }
}
