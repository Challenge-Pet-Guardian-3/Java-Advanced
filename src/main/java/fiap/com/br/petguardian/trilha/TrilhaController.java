package fiap.com.br.petguardian.trilha;

import fiap.com.br.petguardian.trilha.dto.TrilhaConclusaoRequest;
import fiap.com.br.petguardian.trilha.dto.TrilhaEtapaResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trilhas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TrilhaController {

    private final TrilhaService trilhaService;

    @GetMapping("/concluidas")
    public ResponseEntity<List<TrilhaEtapaResponse>> listarConcluidas(@RequestParam Long usuarioId) {
        List<TrilhaEtapaResponse> response = trilhaService.listarConcluidas(usuarioId).stream()
                .map(TrilhaEtapaResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pontos/{usuarioId}")
    public ResponseEntity<Integer> pontosTotais(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(trilhaService.calcularPontosTotaisUsuario(usuarioId));
    }

    @PostMapping("/concluir")
    public ResponseEntity<TrilhaEtapaResponse> concluir(@RequestBody @Valid TrilhaConclusaoRequest request) {
        TrilhaEtapaConcluida etapa = trilhaService.concluirEtapa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TrilhaEtapaResponse.fromEntity(etapa));
    }
}