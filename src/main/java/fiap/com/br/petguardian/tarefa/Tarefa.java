package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa")
    private Long id;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @Column(name = "pontos_tarefa", nullable = false)
    private Integer pontosTarefa;

    @Column(name = "criacao", nullable = false)
    private LocalDateTime criacao;

    @Column(name = "prazo", nullable = false)
    private LocalDateTime prazo;

    @Column(name = "conclusao")
    private LocalDateTime conclusao;

    // Identifica o grupo de tarefas geradas pela mesma recorrência.
    // Todas as ocorrências da mesma recorrência possuem o mesmo UUID.
    // Tarefas avulsas possuem este campo como null.
    @Column(name = "grupo_recorrencia_id", length = 36)
    private String grupoRecorrenciaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id_status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id_pet", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_usuario")
    private Usuario usuario;
}