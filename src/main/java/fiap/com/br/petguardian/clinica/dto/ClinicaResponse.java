// src/main/java/fiap/com/br/petguardian/clinica/dto/ClinicaResponse.java
package fiap.com.br.petguardian.clinica.dto;

import fiap.com.br.petguardian.clinica.Clinica;

import java.util.List;

public record ClinicaResponse(
        Long id,
        String nome,
        String rua,
        String numero,
        String bairro,
        Double distanciaKm,
        String telefone,
        Double avaliacao,
        boolean atendimento24h,
        boolean prontoSocorro,
        boolean patrocinada,
        List<String> especialidades
) {
    public static ClinicaResponse fromEntity(Clinica c) {
        return new ClinicaResponse(
                c.getId(),
                c.getNome(),
                c.getRua(),
                c.getNumero(),
                c.getBairro(),
                c.getDistanciaKm(),
                c.getTelefone(),
                c.getAvaliacao(),
                c.isAtendimento24h(),
                c.isProntoSocorro(),
                c.isPatrocinada(),
                c.getEspecialidades()
        );
    }
}