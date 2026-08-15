package fiap.com.br.petguardian.clinica.dto;

import fiap.com.br.petguardian.clinica.Clinica;

public record ClinicaResponse(
        Long id,
        String nome,
        String ddd,
        String numeroTelefone,
        Long enderecoId
) {
    public static ClinicaResponse fromEntity(Clinica clinica) {
        return new ClinicaResponse(
                clinica.getId(),
                clinica.getNome(),
                clinica.getTelefone().getDdd(),
                clinica.getTelefone().getNumero(),
                clinica.getEndereco().getId()
        );
    }
}
