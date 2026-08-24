package fiap.com.br.petguardian.familia.dto;

import fiap.com.br.petguardian.familia.FamiliaMembro;

public record MembroResponse(Long id, Long usuarioId, String nome, String funcao, Integer xp, Boolean responsavelPrincipal) {
    public static MembroResponse fromEntity(FamiliaMembro m) {
        return new MembroResponse(m.getId(), m.getUsuario().getId(), m.getUsuario().getNome(), m.getFuncao(), m.getXp(), m.getResponsavelPrincipal());
    }
}