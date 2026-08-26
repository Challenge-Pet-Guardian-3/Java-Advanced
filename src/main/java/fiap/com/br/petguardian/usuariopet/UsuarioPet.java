package fiap.com.br.petguardian.usuariopet;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario_pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPet {
    @EmbeddedId
    private UsuarioPetId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("petId")
    @JoinColumn(name = "pet_id_pet")
    private Pet pet;

    @Builder.Default
    @Column(name = "respon_princ", nullable = false)
    private boolean responsavelPrincipal = false;

    public static UsuarioPet of(Usuario usuario, Pet pet, boolean responsavelPrincipal) {
        return UsuarioPet.builder()
                .id(new UsuarioPetId(usuario.getId(), pet.getId()))
                .usuario(usuario)
                .pet(pet)
                .responsavelPrincipal(responsavelPrincipal)
                .build();
    }

    public static UsuarioPet principal(Usuario usuario, Pet pet) {
        return of(usuario, pet, true);
    }
}
