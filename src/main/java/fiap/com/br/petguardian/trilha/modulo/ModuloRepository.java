package fiap.com.br.petguardian.trilha.modulo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuloRepository extends JpaRepository<Modulo, Long> {
    List<Modulo> findAllByTrilhaId(Long trilhaId);
}
