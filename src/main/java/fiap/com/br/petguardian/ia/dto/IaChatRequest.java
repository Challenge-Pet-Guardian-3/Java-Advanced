package fiap.com.br.petguardian.ia.dto;

public record IaChatRequest(Long usuarioId, Long petId, String pergunta) {}