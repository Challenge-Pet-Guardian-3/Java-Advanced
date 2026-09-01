package fiap.com.br.petguardian.trilha;

import fiap.com.br.petguardian.trilha.dto.TrilhaConclusaoRequest;
import fiap.com.br.petguardian.trilha.dto.TrilhaEtapaResponse;
import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trilhas")
@RequiredArgsConstructor
public class TrilhaController {

    private final TrilhaService trilhaService;

    @GetMapping("/concluidas")
    public ResponseEntity<List<TrilhaEtapaResponse>> listarConcluidas(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        List<TrilhaEtapaResponse> response = trilhaService.listarConcluidas(usuarioLogado.getId()).stream()
                .map(TrilhaEtapaResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pontos")
    public ResponseEntity<Integer> pontosTotais(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(trilhaService.calcularPontosTotaisUsuario(usuarioLogado.getId()));
    }

    @PostMapping("/concluir")
    public ResponseEntity<TrilhaEtapaResponse> concluir(Authentication authentication, @RequestBody @Valid TrilhaConclusaoRequest request) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        TrilhaEtapaConcluida etapa = trilhaService.concluirEtapa(usuarioLogado.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TrilhaEtapaResponse.fromEntity(etapa));
    }
}