package fiap.com.br.petguardian.familia;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FamiliaMembroRepository extends JpaRepository<FamiliaMembro, Long> {
    Optional<FamiliaMembro> findByUsuarioId(Long usuarioId);
    List<FamiliaMembro> findByFamiliaId(Long familiaId);
}