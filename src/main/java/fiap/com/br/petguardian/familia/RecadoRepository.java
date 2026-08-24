package fiap.com.br.petguardian.familia;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecadoRepository extends JpaRepository<Recado, Long> {
    List<Recado> findByFamiliaIdOrderByDataHoraDesc(Long familiaId);
}