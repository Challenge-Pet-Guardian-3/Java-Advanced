package fiap.com.br.petguardian.trilha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrilhaEtapaConcluidaRepository extends JpaRepository<TrilhaEtapaConcluida, Long> {

    List<TrilhaEtapaConcluida> findAllByUsuarioId(Long usuarioId);

    Optional<TrilhaEtapaConcluida> findByUsuarioIdAndEtapaId(Long usuarioId, String etapaId);

    @Query("select coalesce(sum(e.xpGanho), 0) from TrilhaEtapaConcluida e where e.usuario.id = :usuarioId")
    Integer somarXpByUsuarioId(@Param("usuarioId") Long usuarioId);
}