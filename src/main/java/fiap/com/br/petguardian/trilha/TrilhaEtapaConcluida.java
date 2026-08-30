package fiap.com.br.petguardian.trilha;

import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "trilha_etapa_concluida", uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id_usuario", "etapa_id"}))
public class TrilhaEtapaConcluida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trilha_etapa")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "etapa_id", nullable = false, length = 20)
    private String etapaId;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "xp_ganho", nullable = false)
    private Integer xpGanho;

    @Column(name = "data_conclusao", nullable = false)
    private LocalDateTime dataConclusao;
}