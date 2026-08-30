package fiap.com.br.petguardian.familia.dto;

import fiap.com.br.petguardian.familia.FamiliaMembro;
import fiap.com.br.petguardian.usuario.Usuario;

public record MembroResponse(
        Long id,
        Long usuarioId,
        String nome,
        String funcao,
        Integer xp,
        Boolean responsavelPrincipal,
        String telefone,
        String cep
) {
    public static MembroResponse fromEntity(FamiliaMembro m) {
        Usuario usuario = m.getUsuario();
        String cep = usuario.getEndereco() != null ? usuario.getEndereco().getCep() : null;

        return new MembroResponse(
                m.getId(),
                usuario.getId(),
                usuario.getNome(),
                m.getFuncao(),
                m.getXp(),
                m.getResponsavelPrincipal(),
                usuario.getTelefone(),
                cep
        );
    }
}