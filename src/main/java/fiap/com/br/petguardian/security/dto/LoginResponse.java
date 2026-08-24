package fiap.com.br.petguardian.security.dto;

public record LoginResponse(String token, Long id, String nome, String email) {}