package fiap.com.br.petguardian.tarefa;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fiap.com.br.petguardian.status.Status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    @Query("select t.id from Tarefa t where t.usuario.id = :usuarioId")
    Set<Long> findIdsByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("select count(t) from Tarefa t where t.usuario.id = :usuarioId and t.prazo >= :inicio and t.prazo < :fim")
    long countByUsuarioIdAndPrazoBetween(
            @Param("usuarioId") Long usuarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("select count(t) from Tarefa t where t.usuario.id = :usuarioId and t.prazo >= :inicio and t.prazo < :fim and t.status = :status")
    long countByUsuarioIdAndPrazoBetweenAndStatus(
            @Param("usuarioId") Long usuarioId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") Status status
    );

    @Query("select distinct t.usuario.id from Tarefa t where t.usuario.id is not null and t.prazo >= :inicio and t.prazo < :fim")
    Set<Long> findDistinctUsuarioIdsComTarefasNoPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("select t from Tarefa t where t.usuario.id = :usuarioId")
    List<Tarefa> findAllByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("select t from Tarefa t " +
            "join t.pet p " +
            "join p.usuarioPets up " +
            "where t.id = :id and up.usuario.id = :usuarioId")
    Optional<Tarefa> findByIdAndUsuarioId(@Param("id") Long id, @Param("usuarioId") Long usuarioId);

    @Query("select t from Tarefa t " +
            "join t.pet p " +
            "join p.usuarioPets up " +
            "where up.usuario.id = :usuarioId " +
            "and t.status.nome_status = fiap.com.br.petguardian.status.EnumStatus.PENDENTE")
    Page<Tarefa> findTarefasPendentesDoCuidador(@Param("usuarioId") Long usuarioId, Pageable pageable);

    @Query("select coalesce(sum(t.pontosTarefa), 0) from Tarefa t " +
            "where t.usuario.id = :usuarioId " +
            "and t.status.nome_status = fiap.com.br.petguardian.status.EnumStatus.CONCLUIDO")
    Integer calcularPontosTotaisUsuario(@Param("usuarioId") Long usuarioId);

    @Query("select t from Tarefa t where t.pet.id = :petId and t.status.nome_status = fiap.com.br.petguardian.status.EnumStatus.CONCLUIDO order by t.conclusao desc")
    List<Tarefa> findConcluidasByPetId(@Param("petId") Long petId);

    @Query("select t.id from Tarefa t where t.pet.id = :petId")
    List<Long> findIdsByPetId(@Param("petId") Long petId);

    @Query("select count(t) from Tarefa t where t.pet.id in :petIds and t.status.nome_status = fiap.com.br.petguardian.status.EnumStatus.PENDENTE")
    int countPendentesByPetIdIn(@Param("petIds") List<Long> petIds);

    @Query("select count(t) from Tarefa t where t.pet.id in :petIds and t.status.nome_status = fiap.com.br.petguardian.status.EnumStatus.CONCLUIDO")
    int countConcluidasByPetIdIn(@Param("petIds") List<Long> petIds);

    @Modifying
    @Transactional
    @Query("update Tarefa t set t.status = :expirada where t.status = :pendente and t.prazo < :agora")
    void expirarTarefasPendentesAtrasadas(
            @Param("agora") LocalDateTime agora,
            @Param("pendente") Status pendente,
            @Param("expirada") Status expirada
    );
}
