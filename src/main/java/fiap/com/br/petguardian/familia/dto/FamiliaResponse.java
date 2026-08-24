package fiap.com.br.petguardian.familia.dto;

import fiap.com.br.petguardian.familia.Familia;
import fiap.com.br.petguardian.familia.Recado;
import java.util.Comparator;
import java.util.List;

public record FamiliaResponse(Long id, String nome, String codigoConvite, Integer xpTotal, List<MembroResponse> membros, List<RecadoResponse> recados) {
    public static FamiliaResponse fromEntity(Familia f) {
        List<MembroResponse> membros = f.getMembros().stream().map(MembroResponse::fromEntity).toList();
        int xpTotal = membros.stream().mapToInt(MembroResponse::xp).sum();
        List<RecadoResponse> recados = f.getRecados().stream()
                .sorted(Comparator.comparing(Recado::getDataHora).reversed())
                .map(RecadoResponse::fromEntity)
                .toList();
        return new FamiliaResponse(f.getId(), f.getNome(), f.getCodigoConvite(), xpTotal, membros, recados);
    }
}