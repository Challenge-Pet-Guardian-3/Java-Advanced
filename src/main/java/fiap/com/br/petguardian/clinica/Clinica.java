// src/main/java/fiap/com/br/petguardian/clinica/Clinica.java
package fiap.com.br.petguardian.clinica;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "clinica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clinica")
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private String numero;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private Double avaliacao;

    @Column(name = "distancia_km")
    private Double distanciaKm;

    @Column(name = "atendimento_24h", nullable = false)
    private boolean atendimento24h;

    @Column(name = "pronto_socorro", nullable = false)
    private boolean prontoSocorro;

    @Column(nullable = false)
    private boolean patrocinada;

    @ElementCollection
    @CollectionTable(name = "clinica_especialidade", joinColumns = @JoinColumn(name = "clinica_id_clinica"))
    @Column(name = "especialidade")
    private List<String> especialidades;
}