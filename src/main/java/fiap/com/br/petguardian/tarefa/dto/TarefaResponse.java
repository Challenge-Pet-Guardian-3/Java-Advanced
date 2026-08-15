package fiap.com.br.petguardian.tarefa.dto;

import fiap.com.br.petguardian.tarefa.Tarefa;

import java.time.LocalDateTime;

public record TarefaResponse(
        Long id,
        String titulo,
        Integer pontosTarefa,
        String descricao,
        LocalDateTime criacao,
        LocalDateTime prazo,
        String status,
        Long usuarioId,
        Long petId,
        Long veterinarioId
) {
    public static TarefaResponse fromEntity(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getPontosTarefa(),
                tarefa.getDescricao(),
                tarefa.getCriacao(),
                tarefa.getPrazo(),
                tarefa.getStatus().getNome_status().name(),
                tarefa.getUsuario() == null ? null : tarefa.getUsuario().getId(),
                tarefa.getPet().getId(),
                tarefa.getVeterinario().getId()
        );
    }
}
