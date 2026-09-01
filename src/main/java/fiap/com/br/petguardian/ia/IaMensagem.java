package fiap.com.br.petguardian.ia;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ia_mensagem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IaMensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ia_mensagem")
    private Long id;

    @Column(name = "usuario_id_usuario", nullable = false)
    private Long usuarioId;

    @Column(name = "pet_id_pet")
    private Long petId;

    @Column(nullable = false, length = 500)
    private String pergunta;

    @Column(nullable = false, length = 1000)
    private String resposta;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
}