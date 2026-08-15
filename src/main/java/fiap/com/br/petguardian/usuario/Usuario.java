package fiap.com.br.petguardian.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.tarefa.Tarefa;
import fiap.com.br.petguardian.telefone.Telefone;
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
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
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

    @ManyToMany
    @JoinTable(
        name = "usuario_endereco",
        joinColumns = @JoinColumn(name = "usuario_id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "endereco_id_endereco")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Endereco> enderecos = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Tarefa> tarefas = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<UsuarioPet> usuarioPets = new HashSet<>();
}
