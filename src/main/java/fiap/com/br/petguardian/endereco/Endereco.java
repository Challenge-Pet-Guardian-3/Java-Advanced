package fiap.com.br.petguardian.endereco;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.endereco.bairro.Bairro;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.clinica.Clinica;
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
@Table(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    private Long id;

    @Column(nullable = false, length = 8)
    private String cep;

    @Column(nullable = false, length = 5)
    private String numero;

    @Column(nullable = false, length = 150)
    private String rua;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bairro_id_bairro")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Bairro bairro;

    @ManyToMany(mappedBy = "enderecos")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Usuario> usuarios = new HashSet<>();

    @OneToMany(mappedBy = "endereco")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Clinica> clinicas = new HashSet<>();
}
