package fiap.com.br.petguardian.usuariopet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UsuarioPetRepository extends JpaRepository<UsuarioPet, UsuarioPetId> {
    @Query("select case when count(up) > 0 then true else false end from UsuarioPet up where up.usuario.id = :usuarioId and up.pet.id = :petId")
    boolean existsByUsuarioIdAndPetId(@Param("usuarioId") Long usuarioId, @Param("petId") Long petId);

    @Query("select case when count(up) > 0 then true else false end from UsuarioPet up where up.usuario.id = :usuarioId and up.pet.id = :petId and up.responsavelPrincipal = true")
    boolean isResponsavelPrincipal(@Param("usuarioId") Long usuarioId, @Param("petId") Long petId);

    @Query("select up from UsuarioPet up where up.usuario.id = :usuarioId and up.pet.id = :petId")
    Optional<UsuarioPet> findByUsuarioIdAndPetId(@Param("usuarioId") Long usuarioId, @Param("petId") Long petId);

    @Query("select up from UsuarioPet up join fetch up.pet join fetch up.usuario where up.usuario.id = :usuarioId")
    List<UsuarioPet> findAllByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("select up from UsuarioPet up join fetch up.pet join fetch up.usuario where up.pet.id in :petIds")
    List<UsuarioPet> findAllByPetIdIn(@Param("petIds") List<Long> petIds);

    @Modifying
    @Transactional
    @Query("update UsuarioPet up set up.responsavelPrincipal = false where up.pet.id = :petId")
    void limparResponsavelPrincipalPorPet(@Param("petId") Long petId);

    @Modifying
    @Transactional
    @Query("delete from UsuarioPet up where up.pet.id = :petId")
    void deleteByPetId(@Param("petId") Long petId);
}
