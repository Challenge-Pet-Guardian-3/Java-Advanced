// src/main/java/fiap/com/br/petguardian/clinica/ClinicaRepository.java
package fiap.com.br.petguardian.clinica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {

    @Query("""
        SELECT c FROM Clinica c
        WHERE (:termo IS NULL OR :termo = ''
            OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')))
        AND (:atendimento24h IS NULL OR c.atendimento24h = :atendimento24h)
        AND (:prontoSocorro IS NULL OR c.prontoSocorro = :prontoSocorro)
        ORDER BY c.patrocinada DESC, c.avaliacao DESC
    """)
    List<Clinica> buscar(
            @Param("termo") String termo,
            @Param("atendimento24h") Boolean atendimento24h,
            @Param("prontoSocorro") Boolean prontoSocorro
    );
}