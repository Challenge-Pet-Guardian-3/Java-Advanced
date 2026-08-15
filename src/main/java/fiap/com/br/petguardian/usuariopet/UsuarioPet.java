package fiap.com.br.petguardian.usuariopet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuario_pet")
public class UsuarioPet {
    @EmbeddedId
    private UsuarioPetId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id_usuario")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("petId")
    @JoinColumn(name = "pet_id_pet")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Pet pet;

    @Builder.Default
    @Column(name = "respon_princ")
    private Boolean responsavelPrincipal = Boolean.FALSE;

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
