package fiap.com.br.petguardian.veterinario.dto;

import fiap.com.br.petguardian.veterinario.Veterinario;

public record VeterinarioResponse(
        Long id,
        String nome,
        String email,
        String ddd,
        String numeroTelefone,
        Long clinicaId
) {
    public static VeterinarioResponse fromEntity(Veterinario veterinario) {
        return new VeterinarioResponse(
                veterinario.getId(),
                veterinario.getNome(),
                veterinario.getEmail(),
                veterinario.getTelefone().getDdd(),
                veterinario.getTelefone().getNumero(),
                veterinario.getClinica() == null ? null : veterinario.getClinica().getId()
        );
    }
}
