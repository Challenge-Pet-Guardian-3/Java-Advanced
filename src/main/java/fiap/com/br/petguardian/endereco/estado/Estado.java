package fiap.com.br.petguardian.endereco.estado;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long id;

    @Column(name = "nome_estado", nullable = false, length = 30)
    private String nome;

    @OneToMany(mappedBy = "estado")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Cidade> cidades = new HashSet<>();
}
