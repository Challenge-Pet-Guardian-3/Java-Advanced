package fiap.com.br.petguardian.pet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("select distinct p from Pet p join p.usuarioPets up where up.usuario.id = :usuarioId")
    List<Pet> findAllByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("select distinct p.id from Pet p join p.usuarioPets up where up.usuario.id = :usuarioId")
    Set<Long> findIdsByUsuarioId(@Param("usuarioId") Long usuarioId);

    Page<Pet> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
