package fiap.com.br.petguardian.usuario.dto;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.usuario.Usuario;

import java.util.Set;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String ddd,
        String numeroTelefone,
        Set<Endereco> enderecos
) {
    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone().getDdd(),
                usuario.getTelefone().getNumero(),
                usuario.getEnderecos()
        );
    }
}
