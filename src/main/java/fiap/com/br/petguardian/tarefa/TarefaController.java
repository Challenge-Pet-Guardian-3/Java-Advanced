package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.tarefa.dto.TarefaConclusaoRequest;
import fiap.com.br.petguardian.tarefa.dto.TarefaRecorrenteRequest;
import fiap.com.br.petguardian.tarefa.dto.TarefaRequest;
import fiap.com.br.petguardian.tarefa.dto.TarefaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TarefaController {

    private final TarefaService tarefaService;

    @GetMapping
    public ResponseEntity<Page<TarefaResponse>> listar(
            @RequestParam(required = false) Long usuarioId,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<Tarefa> tarefas = (usuarioId != null)
                ? tarefaService.findAllByFamilia(usuarioId, pageable)
                : tarefaService.findAll(pageable);

        return ResponseEntity.ok(
                tarefas.map(TarefaResponse::fromEntity)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponse> buscarPorId(
            @PathVariable Long id) {

        Tarefa tarefa = tarefaService.findById(id);

        return ResponseEntity.ok(
                TarefaResponse.fromEntity(tarefa)
        );
    }

    @GetMapping("/pontos/{usuarioId}")
    public ResponseEntity<Integer> pontosTotais(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                tarefaService.calcularPontosTotaisUsuario(usuarioId)
        );
    }

    @PostMapping
    public ResponseEntity<TarefaResponse> criar(
            @RequestBody @Valid TarefaRequest request) {

        Tarefa tarefa = tarefaService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TarefaResponse.fromEntity(tarefa));
    }

    @PostMapping("/recorrente")
    public ResponseEntity<List<TarefaResponse>> criarRecorrente(
            @RequestBody @Valid TarefaRecorrenteRequest request) {

        List<Tarefa> tarefas = tarefaService.createRecorrente(request);

        List<TarefaResponse> response = tarefas.stream()
                .map(TarefaResponse::fromEntity)
                .toList();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid TarefaRequest request) {

        Tarefa tarefa = tarefaService.update(id, request);

        return ResponseEntity.ok(
                TarefaResponse.fromEntity(tarefa)
        );
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<TarefaResponse> concluir(
            @PathVariable Long id,
            @RequestBody @Valid TarefaConclusaoRequest request) {

        Tarefa tarefa = tarefaService.concluir(id, request);

        return ResponseEntity.ok(
                TarefaResponse.fromEntity(tarefa)
        );
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<TarefaResponse> reabrir(
            @PathVariable Long id,
            @RequestParam(required = false) Long solicitanteId) {

        Tarefa tarefa = tarefaService.reabrir(id, solicitanteId);

        return ResponseEntity.ok(
                TarefaResponse.fromEntity(tarefa)
        );
    }

    // PARA TODA A RECORRÊNCIA
    @DeleteMapping("/recorrencia/{grupoRecorrenciaId}")
    public ResponseEntity<Void> pararRecorrencia(
            @PathVariable String grupoRecorrenciaId,
            @RequestParam(required = false) Long solicitanteId) {

        tarefaService.pararRecorrencia(
                grupoRecorrenciaId,
                solicitanteId
        );

        return ResponseEntity.noContent().build();
    }

    // DELETA UMA ÚNICA TAREFA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestParam(required = false) Long solicitanteId) {

        tarefaService.delete(id, solicitanteId);

        return ResponseEntity.noContent().build();
    }
}