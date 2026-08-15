package fiap.com.br.petguardian.pet.raca;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "raca")
public class Raca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_raca")
    private Long id;

    @Column(name = "nome_raca", nullable = false, length = 30)
    private String nome;
}
