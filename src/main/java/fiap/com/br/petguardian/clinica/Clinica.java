package fiap.com.br.petguardian.clinica;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.telefone.Telefone;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clinica")
public class Clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clinica")
    private Long id;

    @Column(nullable = false, length = 30)
    private String nome;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "telefone_id_telefone", nullable = false, unique = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Telefone telefone;

    @ManyToOne
    @JoinColumn(name = "endereco_id_endereco", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Endereco endereco;
}
