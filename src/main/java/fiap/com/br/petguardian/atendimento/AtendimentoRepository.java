package fiap.com.br.petguardian.atendimento;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fiap.com.br.petguardian.status.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    @Query("select distinct a from Atendimento a join a.pet p join p.usuarioPets up where up.usuario.id = :usuarioId")
    Page<Atendimento> findAllByUsuarioId(@Param("usuarioId") Long usuarioId, Pageable pageable);

    List<Atendimento> findAllByPetIdOrderByDataDesc(Long petId);

    @Query("select a.id from Atendimento a where a.pet.id = :petId")
    List<Long> findIdsByPetId(@Param("petId") Long petId);

    @Query("select count(a) from Atendimento a where a.pet.id in :petIds")
    int countByPetIdIn(@Param("petIds") List<Long> petIds);

    @Query("select distinct a.id from Atendimento a join a.pet p join p.usuarioPets up where up.usuario.id = :usuarioId")
    Set<Long> findIdsByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Transactional
    @Query("update Atendimento a set a.status = :expirada where a.status = :pendente and a.data < :agora")
    void expirarAtendimentosPendentesAtrasados(
            @Param("agora") LocalDateTime agora,
            @Param("pendente") Status pendente,
            @Param("expirada") Status expirada
    );
}
