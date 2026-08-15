package fiap.com.br.petguardian.usuariopet;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UsuarioPetId implements Serializable {
    @Column(name = "usuario_id_usuario")
    private Long usuarioId;

    @Column(name = "pet_id_pet")
    private Long petId;
}
