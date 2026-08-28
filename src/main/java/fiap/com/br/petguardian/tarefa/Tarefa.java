package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.tarefa.status.EnumStatus;
import fiap.com.br.petguardian.tarefa.status.Status;
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

    @Column(nullable = false, length = 30)
    private String titulo;

    @Column(name = "pontos_tarefa", nullable = false)
    private Integer pontosTarefa;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime criacao;

    @Column(nullable = false)
    private LocalDateTime prazo;

    @Column(nullable = true)
    private LocalDateTime conclusao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id_status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id_pet", nullable = false)
    private Pet pet;

    public boolean estaPendente() {
        return this.status != null && this.status.getNomeStatus() == EnumStatus.PENDENTE;
    }

    public void concluir(Usuario usuario, Status statusConcluido, LocalDateTime conclusao) {
        this.usuario = usuario;
        this.status = statusConcluido;
        this.conclusao = conclusao;
    }
}
