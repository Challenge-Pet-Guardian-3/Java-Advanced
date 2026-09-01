package fiap.com.br.petguardian.ia;

import fiap.com.br.petguardian.ia.dto.IaInsightResponse;
import fiap.com.br.petguardian.ia.dto.IaMensagemResponse;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IaService {

    private final PetRepository petRepository;
    private final IaMensagemRepository iaMensagemRepository;

    public String responder(Long usuarioId, Long petId, String pergunta) {
        String resposta = gerarResposta(pergunta);

        IaMensagem mensagem = IaMensagem.builder()
                .usuarioId(usuarioId)
                .petId(petId)
                .pergunta(pergunta)
                .resposta(resposta)
                .dataHora(LocalDateTime.now())
                .build();

        iaMensagemRepository.save(mensagem);

        return resposta;
    }

    private String gerarResposta(String pergunta) {
        String perguntaNormalizada = pergunta.toLowerCase();

        if (perguntaNormalizada.contains("ração") || perguntaNormalizada.contains("racao")) {
            return "A quantidade ideal de ração varia por porte e idade do animal. "
                    + "Como regra geral, siga a tabela de gramatura da embalagem baseada no peso do pet, "
                    + "dividindo em 2 a 3 refeições ao dia. Consulte um veterinário para uma dieta personalizada.";
        }
        if (perguntaNormalizada.contains("vacina")) {
            return "As vacinas essenciais incluem V8/V10 (cães) ou V4/V5 (gatos) e a Antirrábica, "
                    + "com reforços anuais. O calendário exato depende da idade e histórico do pet — "
                    + "um veterinário pode confirmar o protocolo mais adequado.";
        }
        if (perguntaNormalizada.contains("ansiedade") || perguntaNormalizada.contains("ansioso")) {
            return "Rotinas previsíveis, exercícios diários e enriquecimento ambiental (como os da aba Trilhas) "
                    + "ajudam bastante a reduzir a ansiedade. Em casos persistentes, vale buscar orientação veterinária.";
        }
        if (perguntaNormalizada.contains("banho")) {
            return "A frequência recomendada de banho é a cada 15-30 dias para a maioria dos pets, "
                    + "podendo variar conforme o tipo de pelagem e a rotina do animal.";
        }
        if (perguntaNormalizada.contains("tóxico") || perguntaNormalizada.contains("toxico")) {
            return "Chocolate, cebola, alho, uvas/passas, cafeína e adoçantes com xilitol são tóxicos "
                    + "para cães e gatos. Em caso de ingestão, procure atendimento veterinário imediatamente.";
        }
        if (perguntaNormalizada.contains("dente") || perguntaNormalizada.contains("escovar")) {
            return "A escovação dos dentes deve ser feita idealmente 2 a 3 vezes por semana, "
                    + "com pasta de dente própria para pets (nunca use pasta de dente humana). "
                    + "Introduza o hábito aos poucos para o animal se acostumar.";
        }

        return "Ainda não tenho uma resposta específica para essa pergunta. "
                + "Recomendo consultar um médico veterinário para orientação precisa sobre o caso do seu pet.";
    }

    public List<IaInsightResponse> gerarInsights(Long petId) {
        List<IaInsightResponse> insights = new ArrayList<>();

        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return insights;
        }

        insights.add(new IaInsightResponse(
                "Rotina de cuidados",
                "Manter tarefas diárias registradas ajuda a identificar padrões de saúde do " + pet.getNome() + "."
        ));
        insights.add(new IaInsightResponse(
                "Enriquecimento ambiental",
                "Considere completar as Trilhas de Aprendizado para reforçar o bem-estar mental do pet."
        ));

        return insights;
    }

    public List<IaMensagemResponse> buscarHistorico(Long usuarioId) {
        return iaMensagemRepository.findByUsuarioIdOrderByDataHoraAsc(usuarioId)
                .stream()
                .map(IaMensagemResponse::fromEntity)
                .toList();
    }
}