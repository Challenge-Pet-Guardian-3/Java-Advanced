package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.tarefa.status.EnumStatus;
import fiap.com.br.petguardian.tarefa.status.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query("select t from Tarefa t " +
            "join t.pet p " +
            "join p.usuarioPets up " +
            "where t.id = :id and up.usuario.id = :usuarioId")
    Optional<Tarefa> findByIdAndUsuarioId(@Param("id") Long id, @Param("usuarioId") Long usuarioId);

    @Query("select t from Tarefa t " +
            "join t.pet p " +
            "join p.usuarioPets up " +
            "where up.usuario.id = :usuarioId " +
            "and t.status.nomeStatus = :status")
    Page<Tarefa> findTarefasPendentesDoCuidador(
            @Param("usuarioId") Long usuarioId,
            @Param("status") EnumStatus status,
            Pageable pageable);

    @Query("select coalesce(sum(t.pontosTarefa), 0) from Tarefa t " +
            "where t.usuario.id = :usuarioId " +
            "and t.status.nomeStatus = :status")
    Integer calcularPontosTotaisUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("status") EnumStatus status);

    @Query("select coalesce(sum(t.pontosTarefa), 0) from Tarefa t " +
            "where t.pet.id = :petId " +
            "and t.status.nomeStatus = :status")
    Integer calcularPontosTarefasPorPet(
            @Param("petId") Long petId,
            @Param("status") EnumStatus status);

    @Query("select t from Tarefa t where t.pet.id = :petId and t.status.nomeStatus = :status order by t.conclusao desc")
    List<Tarefa> findConcluidasByPetId(
            @Param("petId") Long petId,
            @Param("status") EnumStatus status);

    @Query("select t.pet.id, t.id from Tarefa t where t.pet.id in :petIds")
    List<Object[]> findTarefaIdsByPetIdIn(@Param("petIds") List<Long> petIds);

    @Query("select count(t) from Tarefa t where t.pet.id in :petIds and t.status.nomeStatus = :status")
    int countByPetIdInAndStatus(
            @Param("petIds") List<Long> petIds,
            @Param("status") EnumStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update Tarefa t set t.status = :expirada where t.status = :pendente and t.prazo < :agora")
    void expirarTarefasPendentesAtrasadas(
            @Param("agora") LocalDateTime agora,
            @Param("pendente") Status pendente,
            @Param("expirada") Status expirada
    );
}
