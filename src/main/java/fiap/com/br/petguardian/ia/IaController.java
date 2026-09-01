package fiap.com.br.petguardian.ia;

import fiap.com.br.petguardian.ia.dto.IaChatRequest;
import fiap.com.br.petguardian.ia.dto.IaChatResponse;
import fiap.com.br.petguardian.ia.dto.IaInsightResponse;
import fiap.com.br.petguardian.ia.dto.IaMensagemResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ia")
@RequiredArgsConstructor
@Tag(name = "Assistente de IA", description = "Orientações preventivas e chat sobre cuidados com pets")
public class IaController {

    private final IaService iaService;

    @PostMapping("/chat")
    public IaChatResponse chat(@RequestBody IaChatRequest request) {
        String resposta = iaService.responder(request.usuarioId(), request.petId(), request.pergunta());
        return new IaChatResponse(resposta);
    }

    @GetMapping("/insights/{petId}")
    public List<IaInsightResponse> insights(@PathVariable Long petId) {
        return iaService.gerarInsights(petId);
    }

    @GetMapping("/historico/{usuarioId}")
    public List<IaMensagemResponse> historico(@PathVariable Long usuarioId) {
        return iaService.buscarHistorico(usuarioId);
    }
}