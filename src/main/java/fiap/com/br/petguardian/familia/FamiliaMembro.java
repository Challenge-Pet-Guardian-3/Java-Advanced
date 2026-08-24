package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "familia_membro", uniqueConstraints = @UniqueConstraint(columnNames = {"familia_id_familia", "usuario_id_usuario"}))
public class FamiliaMembro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_familia_membro")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "familia_id_familia", nullable = false)
    private Familia familia;

    @ManyToOne
    @JoinColumn(name = "usuario_id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 40)
    private String funcao;

    @Column(nullable = false)
    @Builder.Default
    private Integer xp = 0;

    @Column(name = "responsavel_principal", nullable = false)
    @Builder.Default
    private Boolean responsavelPrincipal = false;

    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;
}