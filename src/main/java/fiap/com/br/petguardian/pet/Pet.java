package fiap.com.br.petguardian.pet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.atendimento.Atendimento;
import fiap.com.br.petguardian.pet.raca.Raca;
import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pet")
    private Long id;

    @Column(nullable = false, length = 30)
    private String nome;

    @Column(nullable = false)
    private Integer idade;

    @ManyToOne
    @JoinColumn(name = "raca_id_raca", nullable = false)
    private Raca raca;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PetPorte porte;

    @Column(nullable = false, length = 1)
    private Character sexo;

    @Column(nullable = false)
    private Boolean castrado;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Tarefa> tarefas = new HashSet<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Atendimento> atendimentos = new HashSet<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<UsuarioPet> usuarioPets = new HashSet<>();
}
