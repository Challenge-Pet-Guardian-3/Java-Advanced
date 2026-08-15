package fiap.com.br.petguardian.tarefa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.veterinario.Veterinario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tarefa")
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

    private LocalDateTime conclusao;

    @ManyToOne
    @JoinColumn(name = "status_id_status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "usuario_id_usuario")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "pet_id_pet", nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "veterinario_id_veterinario", nullable = false)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Veterinario veterinario;
}
