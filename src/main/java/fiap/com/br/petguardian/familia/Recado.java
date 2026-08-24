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
@Table(name = "familia_recado")
public class Recado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recado")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "familia_id_familia", nullable = false)
    private Familia familia;

    @ManyToOne
    @JoinColumn(name = "autor_id_usuario", nullable = false)
    private Usuario autor;

    @Column(nullable = false, length = 500)
    private String texto;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    @Builder.Default
    private Boolean editado = false;
}