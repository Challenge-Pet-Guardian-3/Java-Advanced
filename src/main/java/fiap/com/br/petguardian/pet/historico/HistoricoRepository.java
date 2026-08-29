package fiap.com.br.petguardian.pet.historico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findAllByPetIdOrderByDataHistDesc(Long petId);
}
