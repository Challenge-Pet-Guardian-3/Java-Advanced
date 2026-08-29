package fiap.com.br.petguardian.trilha;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrilhaRepository extends JpaRepository<Trilha, Long> {
    List<Trilha> findAllByPetId(Long petId);
}
