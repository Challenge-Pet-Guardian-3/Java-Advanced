package fiap.com.br.petguardian.telefone;

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
@Table(name = "telefone")
public class Telefone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefone")
    private Long id;

    @Column(name = "num_ddd", nullable = false, length = 2)
    private String ddd;

    @Column(name = "num_tel", nullable = false, length = 9)
    private String numero;
}
