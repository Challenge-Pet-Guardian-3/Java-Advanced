package fiap.com.br.petguardian.endereco.bairro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.cidade.Cidade;
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
@Table(name = "bairro")
public class Bairro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bairro")
    private Long id;

    @Column(name = "nome_bairro", nullable = false, length = 30)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cidade_id_cidade")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Cidade cidade;

    @OneToMany(mappedBy = "bairro")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Endereco> enderecos = new HashSet<>();
}
