package fiap.com.br.petguardian.pet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetPorte;

import java.time.LocalDate;

public record PetResponse(
        Long id,
        String nome,
        Integer idade,
        String raca,
        PetPorte porte,
        Character sexo,
        Boolean castrado,
        Double peso,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate ultimaVacina,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate ultimaConsulta,
        Integer avatarId
) {
    public static PetResponse fromEntity(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getNome(),
                pet.getIdade(),
                pet.getRaca() != null ? pet.getRaca().getNome() : null,
                pet.getPorte(),
                pet.getSexo(),
                pet.getCastrado(),
                pet.getPeso(),
                pet.getUltimaVacina(),
                pet.getUltimaConsulta(),
                pet.getAvatarId()
        );
    }
}