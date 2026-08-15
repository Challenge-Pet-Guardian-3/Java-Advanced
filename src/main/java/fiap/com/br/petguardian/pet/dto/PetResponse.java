package fiap.com.br.petguardian.pet.dto;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetPorte;

public record PetResponse(
        Long id,
        String nome,
        Integer idade,
        String raca,
        PetPorte porte,
        Character sexo,
        Boolean castrado
) {
    public static PetResponse fromEntity(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getNome(),
                pet.getIdade(),
                pet.getRaca().getNome(),
                pet.getPorte(),
                pet.getSexo(),
                pet.getCastrado()
        );
    }
}
