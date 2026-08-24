package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.tarefa.dto.TarefaConclusaoRequest;
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

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TarefaController {

    private final TarefaService tarefaService;

    @GetMapping
    public ResponseEntity<Page<TarefaResponse>> listar(@PageableDefault(size = 20) Pageable pageable) {
        Page<TarefaResponse> page = tarefaService.findAll(pageable).map(TarefaResponse::fromEntity);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponse> buscarPorId(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.findById(id);
        return ResponseEntity.ok(TarefaResponse.fromEntity(tarefa));
    }

    @PostMapping
    public ResponseEntity<TarefaResponse> criar(@RequestBody @Valid TarefaRequest request) {
        Tarefa tarefa = tarefaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TarefaResponse.fromEntity(tarefa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid TarefaRequest request) {
        Tarefa tarefa = tarefaService.update(id, request);
        return ResponseEntity.ok(TarefaResponse.fromEntity(tarefa));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<TarefaResponse> concluir(@PathVariable Long id, @RequestBody @Valid TarefaConclusaoRequest request) {
        Tarefa tarefa = tarefaService.concluir(id, request);
        return ResponseEntity.ok(TarefaResponse.fromEntity(tarefa));
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<TarefaResponse> reabrir(
            @PathVariable Long id,
            @RequestParam(required = false) Long solicitanteId) {
        Tarefa tarefa = tarefaService.reabrir(id, solicitanteId);
        return ResponseEntity.ok(TarefaResponse.fromEntity(tarefa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestParam(required = false) Long solicitanteId) {
        tarefaService.delete(id, solicitanteId);
        return ResponseEntity.noContent().build();
    }
}