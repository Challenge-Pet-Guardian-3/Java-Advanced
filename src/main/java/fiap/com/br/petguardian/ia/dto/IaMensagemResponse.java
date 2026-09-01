package fiap.com.br.petguardian.ia.dto;

import fiap.com.br.petguardian.ia.IaMensagem;

import java.time.LocalDateTime;

public record IaMensagemResponse(
        Long id,
        String pergunta,
        String resposta,
        LocalDateTime dataHora
) {
    public static IaMensagemResponse fromEntity(IaMensagem m) {
        return new IaMensagemResponse(m.getId(), m.getPergunta(), m.getResposta(), m.getDataHora());
    }
}