package fiap.com.br.petguardian.veterinario;

import fiap.com.br.petguardian.clinica.Clinica;
import fiap.com.br.petguardian.telefone.Telefone;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "veterinario")
public class Veterinario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veterinario")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String senha;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "telefone_id_telefone", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Telefone telefone;

    @ManyToOne
    @JoinColumn(name = "clinica_id_clinica")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Clinica clinica;
}
