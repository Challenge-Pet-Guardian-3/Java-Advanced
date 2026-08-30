package fiap.com.br.petguardian.tarefa.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record TarefaRecorrenteRequest(
        @NotBlank(message = "O título é obrigatório.")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória.")
        String descricao,

        @NotNull(message = "Os pontos da tarefa são obrigatórios.")
        @Positive(message = "Pontos da tarefa devem ser maiores que zero.")
        Integer pontosTarefa,

        @NotNull(message = "O ID do pet é obrigatório.")
        Long petId,

        @NotEmpty(message = "Selecione ao menos um dia da semana.")
        Set<DayOfWeek> diasSemana,

        @NotNull(message = "O horário do prazo diário é obrigatório.")
        LocalTime horario,

        // Se não vier, usa hoje como início
        LocalDate dataInicio,

        @NotNull(message = "A data final da recorrência é obrigatória.")
        @FutureOrPresent(message = "A data final não pode estar no passado.")
        LocalDate dataFim
) {}