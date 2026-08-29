package fiap.com.br.petguardian.validation;

import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.tarefa.dto.TarefaRequest;
import fiap.com.br.petguardian.tarefa.status.EnumStatus;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TarefaValidator {

    private final UsuarioPetRepository usuarioPetRepository;

    public void validarCriacao(TarefaRequest request) {
        if (EnumStatus.valueOf(request.status().trim().toUpperCase()) != EnumStatus.PENDENTE) {
            throw new IllegalArgumentException("Uma tarefa nova deve iniciar com status PENDENTE.");
        }

        if (request.usuarioId() != null) {
            throw new IllegalArgumentException(
                    "Tarefa deve ser criada sem usuario executor. Use o endpoint de conclusao para registrar o cuidador.");
        }
    }

    public void validarAtualizacao(
            EnumStatus status,
            Usuario usuario,
            LocalDateTime prazo,
            LocalDateTime agora) {
        if (status == EnumStatus.EXPIRADO && prazo.isAfter(agora)) {
            throw new IllegalArgumentException("Nao e permitido marcar como EXPIRADO antes do vencimento do prazo.");
        }

        if (status != EnumStatus.EXPIRADO && prazo.isBefore(agora)) {
            throw new IllegalArgumentException("Prazo nao pode estar no passado para uma tarefa nao expirada.");
        }

        if (status == EnumStatus.CONCLUIDO && usuario == null) {
            throw new IllegalArgumentException("Uma tarefa concluida deve informar o usuario executor.");
        }

        if (status != EnumStatus.CONCLUIDO && usuario != null) {
            throw new IllegalArgumentException("Somente tarefas concluidas podem possuir usuario executor.");
        }
    }

    public void validarCuidadorDoPet(Long usuarioId, Long petId) {
        if (!usuarioPetRepository.existsByUsuarioIdAndPetId(usuarioId, petId)) {
            throw new IllegalArgumentException("Usuario informado nao esta vinculado ao pet da tarefa.");
        }
    }

    public void validarPendenteParaConclusao(Tarefa tarefa) {
        if (tarefa.getStatus().getNomeStatus() != EnumStatus.PENDENTE) {
            throw new IllegalArgumentException("Apenas tarefas pendentes podem ser concluidas.");
        }
    }
}
