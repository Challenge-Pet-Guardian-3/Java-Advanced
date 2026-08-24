package fiap.com.br.petguardian.usuario.dto;

import fiap.com.br.petguardian.usuario.Usuario;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String ddd,
        String numeroTelefone,
        String token
) {
    public static UsuarioResponse fromEntity(Usuario usuario) {
        return fromEntity(usuario, null);
    }

    public static UsuarioResponse fromEntity(Usuario usuario, String token) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone() != null ? usuario.getTelefone().getDdd() : null,
                usuario.getTelefone() != null ? usuario.getTelefone().getNumero() : null,
                token
        );
    }
}