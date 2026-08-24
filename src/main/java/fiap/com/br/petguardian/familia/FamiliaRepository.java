package fiap.com.br.petguardian.familia;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FamiliaRepository extends JpaRepository<Familia, Long> {
    Optional<Familia> findByCodigoConvite(String codigoConvite);
    boolean existsByCodigoConvite(String codigoConvite);
}