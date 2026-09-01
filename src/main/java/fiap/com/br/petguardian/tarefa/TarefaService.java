package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.familia.FamiliaMembroRepository;
import fiap.com.br.petguardian.familia.XpService;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetPermissaoService;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusService;
import fiap.com.br.petguardian.tarefa.dto.TarefaRecorrenteRequest;
import fiap.com.br.petguardian.tarefa.dto.TarefaRequest;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;
    private final UsuarioPetRepository usuarioPetRepository;
    private final FamiliaMembroRepository familiaMembroRepository;
    private final StatusService statusService;
    private final XpService xpService;
    private final PetPermissaoService petPermissaoService;

    public Page<Tarefa> findAll(Pageable pageable) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findAll(pageable);
    }

    public Page<Tarefa> findAll(Long usuarioId, Pageable pageable) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findTarefasPendentesDoCuidador(usuarioId, pageable);
    }

    public Page<Tarefa> findAllByFamilia(Long usuarioId, Pageable pageable) {
        expirarTarefasPendentesAtrasadas();

        var membroOpt = familiaMembroRepository.findByUsuarioId(usuarioId);
        if (membroOpt.isEmpty()) {
            return tarefaRepository.findTodasDoCuidador(usuarioId, pageable);
        }

        Long familiaId = membroOpt.get().getFamilia().getId();
        List<Long> usuarioIds = familiaMembroRepository.findByFamiliaId(familiaId)
                .stream().map(m -> m.getUsuario().getId()).toList();

        List<Long> petIds = usuarioIds.stream()
                .flatMap(uid -> usuarioPetRepository.findAllByUsuarioId(uid).stream())
                .map(up -> up.getPet().getId())
                .distinct()
                .toList();

        if (petIds.isEmpty()) {
            return Page.empty(pageable);
        }

        return tarefaRepository.findTodasByPetIdIn(petIds, pageable);
    }

    public Tarefa findById(Long id) {
        expirarTarefasPendentesAtrasadas();
        return findTarefaById(id);
    }

    public Tarefa findByUsuarioIdAndTarefaId(Long usuarioId, Long tarefaId) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findByIdAndUsuarioId(tarefaId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa com id " + tarefaId + " não encontrada para o usuário informado."));
    }

    @Transactional
    public Tarefa create(TarefaRequest request) {
        Pet pet = findPetById(request.petId());

        Tarefa tarefa = request.toEntity(null, pet);

        if (tarefa.getCriacao() == null) {
            tarefa.setCriacao(LocalDateTime.now());
        }
        if (tarefa.getPontosTarefa() == null) {
            tarefa.setPontosTarefa(request.pontosTarefa() != null ? request.pontosTarefa() : 15);
        }
        if (tarefa.getPrazo() == null) {
            tarefa.setPrazo(request.prazo() != null ? request.prazo() : LocalDateTime.now().withHour(23).withMinute(59).withSecond(59));
        }

        tarefa.setStatus(statusService.findStatusByNome("PENDENTE"));
        tarefa.setConclusao(null);
        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public List<Tarefa> createRecorrente(TarefaRecorrenteRequest request) {
        Pet pet = findPetById(request.petId());

        LocalDate inicio = request.dataInicio() != null ? request.dataInicio() : LocalDate.now();
        LocalDate fim = request.dataFim();

        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à data de início.");
        }

        Status pendente = statusService.findStatusByNome("PENDENTE");
        LocalDateTime agora = LocalDateTime.now();

        String grupoRecorrenciaId = UUID.randomUUID().toString();

        List<Tarefa> ocorrencias = new ArrayList<>();
        for (LocalDate data = inicio; !data.isAfter(fim); data = data.plusDays(1)) {
            if (!request.diasSemana().contains(data.getDayOfWeek())) {
                continue;
            }

            Tarefa tarefa = Tarefa.builder()
                    .titulo(request.titulo())
                    .descricao(request.descricao())
                    .pontosTarefa(request.pontosTarefa())
                    .criacao(agora)
                    .prazo(data.atTime(request.horario()))
                    .grupoRecorrenciaId(grupoRecorrenciaId)
                    .status(pendente)
                    .pet(pet)
                    .usuario(null)
                    .build();

            ocorrencias.add(tarefa);
        }

        if (ocorrencias.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma ocorrência gerada — confira os dias da semana e o período informado.");
        }

        return tarefaRepository.saveAll(ocorrencias);
    }

    @Transactional
    public Tarefa update(Long id, TarefaRequest request) {
        Tarefa tarefaAtual = findTarefaById(id);
        Pet pet = findPetById(request.petId());

        Usuario usuario = null;
        if (request.usuarioId() != null) {
            usuario = findUsuarioById(request.usuarioId());
            petPermissaoService.validarCuidadorDoPet(usuario.getId(), pet.getId());
        }

        Tarefa tarefa = request.toEntity(usuario, pet);
        tarefa.setId(tarefaAtual.getId());
        tarefa.setCriacao(tarefaAtual.getCriacao() != null ? tarefaAtual.getCriacao() : LocalDateTime.now());
        tarefa.setGrupoRecorrenciaId(tarefaAtual.getGrupoRecorrenciaId());

        String statusStr = request.status() != null ? request.status() : "PENDENTE";
        if ("EXPIRADO".equalsIgnoreCase(statusStr) && request.prazo() != null && request.prazo().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é permitido marcar como EXPIRADO antes do vencimento do prazo.");
        }

        tarefa.setStatus(statusService.findStatusByNome(statusStr));
        if ("CONCLUIDO".equalsIgnoreCase(statusStr)) {
            tarefa.setConclusao(tarefaAtual.getConclusao() == null ? LocalDateTime.now() : tarefaAtual.getConclusao());
        } else {
            tarefa.setConclusao(null);
        }

        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public Tarefa concluir(Long id, Long concluinteId) {
        expirarTarefasPendentesAtrasadas();

        Tarefa tarefa = findTarefaById(id);
        if (!"PENDENTE".equalsIgnoreCase(tarefa.getStatus().getNome_status().name())) {
            throw new IllegalArgumentException("Apenas tarefas pendentes podem ser concluídas.");
        }

        Usuario usuario = findUsuarioById(concluinteId);
        petPermissaoService.validarCuidadorDoPet(usuario.getId(), tarefa.getPet().getId());

        tarefa.setUsuario(usuario);
        tarefa.setStatus(statusService.findStatusByNome("CONCLUIDO"));
        tarefa.setConclusao(LocalDateTime.now());

        int pontos = tarefa.getPontosTarefa() != null ? tarefa.getPontosTarefa() : 15;
        xpService.adicionar(usuario.getId(), pontos);

        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public Tarefa reabrir(Long id, Long solicitanteId) {
        Tarefa tarefa = findTarefaById(id);

        if (!"CONCLUIDO".equalsIgnoreCase(tarefa.getStatus().getNome_status().name())) {
            throw new IllegalArgumentException("Apenas tarefas concluídas podem ser reabertas.");
        }

        if (tarefa.getUsuario() != null) {
            boolean ehProprioUsuario = tarefa.getUsuario().getId().equals(solicitanteId);
            boolean ehDonoFamilia = familiaMembroRepository.findByUsuarioId(solicitanteId)
                    .map(m -> Boolean.TRUE.equals(m.getResponsavelPrincipal()))
                    .orElse(false);

            if (!ehProprioUsuario && !ehDonoFamilia) {
                throw new IllegalArgumentException("Você não pode desmarcar uma tarefa realizada por outro cuidador.");
            }
        }

        if (tarefa.getUsuario() != null) {
            Long usuarioConclusaoId = tarefa.getUsuario().getId();
            int pontos = tarefa.getPontosTarefa() != null ? tarefa.getPontosTarefa() : 15;
            xpService.remover(usuarioConclusaoId, pontos);
        }

        tarefa.setStatus(statusService.findStatusByNome("PENDENTE"));
        tarefa.setUsuario(null);
        tarefa.setConclusao(null);
        return tarefaRepository.save(tarefa);
    }

    public Integer calcularPontosTotaisUsuario(Long usuarioId) {
        findUsuarioById(usuarioId);
        Integer pontos = tarefaRepository.calcularPontosTotaisUsuario(usuarioId);
        return pontos != null ? pontos : 0;
    }

    @Transactional
    public void delete(Long id, Long solicitanteId) {
        Tarefa tarefa = findTarefaById(id);

        findUsuarioById(solicitanteId);

        boolean isDonoFamilia = familiaMembroRepository.findByUsuarioId(solicitanteId)
                .map(m -> Boolean.TRUE.equals(m.getResponsavelPrincipal()))
                .orElse(false);

        boolean isDonoPet = usuarioPetRepository.findAllByUsuarioId(solicitanteId).stream()
                .anyMatch(up -> up.getPet().getId().equals(tarefa.getPet().getId()) && Boolean.TRUE.equals(up.getResponsavelPrincipal()));

        if (!isDonoFamilia && !isDonoPet) {
            throw new IllegalArgumentException("Apenas o responsável pela família ou o dono do pet tem permissão para excluir tarefas.");
        }

        if ("CONCLUIDO".equalsIgnoreCase(tarefa.getStatus().getNome_status().name()) && tarefa.getUsuario() != null) {
            int pontos = tarefa.getPontosTarefa() != null ? tarefa.getPontosTarefa() : 15;
            xpService.remover(tarefa.getUsuario().getId(), pontos);
        }

        tarefaRepository.delete(tarefa);
    }

    @Transactional
    public int pararRecorrencia(String grupoRecorrenciaId, Long solicitanteId) {
        List<Tarefa> ocorrencias = tarefaRepository.findByGrupoRecorrenciaId(grupoRecorrenciaId);
        if (ocorrencias.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma ocorrência encontrada para essa recorrência.");
        }

        findUsuarioById(solicitanteId);

        Tarefa referencia = ocorrencias.get(0);
        boolean isDonoFamilia = familiaMembroRepository.findByUsuarioId(solicitanteId)
                .map(m -> Boolean.TRUE.equals(m.getResponsavelPrincipal()))
                .orElse(false);

        boolean isDonoPet = usuarioPetRepository.findAllByUsuarioId(solicitanteId).stream()
                .anyMatch(up -> up.getPet().getId().equals(referencia.getPet().getId()) && Boolean.TRUE.equals(up.getResponsavelPrincipal()));

        if (!isDonoFamilia && !isDonoPet) {
            throw new IllegalArgumentException("Apenas o responsável pela família ou o dono do pet tem permissão para parar essa recorrência.");
        }

        return tarefaRepository.excluirOcorrenciasFuturasPorGrupo(grupoRecorrenciaId, LocalDateTime.now());
    }

    private Tarefa findTarefaById(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa com id " + id + " não encontrada."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com id " + id + " não encontrado."));
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com id " + id + " não encontrado."));
    }

    private void expirarTarefasPendentesAtrasadas() {
        Status pendente = statusService.findStatusByNome("PENDENTE");
        Status expirado = statusService.findStatusByNome("EXPIRADO");
        tarefaRepository.expirarTarefasPendentesAtrasadas(LocalDateTime.now(), pendente, expirado);
    }
}