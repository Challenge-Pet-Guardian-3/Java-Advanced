package fiap.com.br.petguardian.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    @Query("select s from Status s where s.nome_status = :nomeStatus")
    Optional<Status> findByNomeStatus(@Param("nomeStatus") EnumStatus nomeStatus);
}
