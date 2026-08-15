package fiap.com.br.petguardian.veterinario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {
    Page<Veterinario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Optional<Veterinario> findByEmailIgnoreCase(String email);
}
