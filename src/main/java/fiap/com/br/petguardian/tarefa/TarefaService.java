package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.tarefa.status.EnumStatus;
import fiap.com.br.petguardian.tarefa.status.Status;
import fiap.com.br.petguardian.tarefa.status.StatusService;
import fiap.com.br.petguardian.tarefa.dto.TarefaConclusaoRequest;
import fiap.com.br.petguardian.tarefa.dto.TarefaRequest;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;
    private final UsuarioPetRepository usuarioPetRepository;
    private final StatusService statusService;

    @Transactional(readOnly = true)
    public Page<Tarefa> findAll(Pageable pageable) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Tarefa> findAll(Long usuarioId, Pageable pageable) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findTarefasPendentesDoCuidador(usuarioId, EnumStatus.PENDENTE, pageable);
    }

    @Transactional(readOnly = true)
    public Tarefa findById(Long id) {
        expirarTarefasPendentesAtrasadas();
        return findTarefaById(id);
    }

    @Transactional(readOnly = true)
    public Tarefa findByUsuarioIdAndTarefaId(Long usuarioId, Long tarefaId) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findByIdAndUsuarioId(tarefaId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tarefa com id " + tarefaId + " nao encontrada para o usuario informado."));
    }

    @Transactional
    public Tarefa create(TarefaRequest request) {
        validarCriacao(request);
        LocalDateTime agora = LocalDateTime.now();
        Pet pet = findPetById(request.petId());
        Status statusPendente = statusService.findStatusByNome(EnumStatus.PENDENTE);
        return tarefaRepository.save(request.toEntity(pet, statusPendente, agora));
    }

    @Transactional
    public Tarefa update(Long id, TarefaRequest request) {
        Tarefa tarefaAtual = findTarefaById(id);
        Pet pet = findPetById(request.petId());
        EnumStatus status = request.statusEnum();
        LocalDateTime agora = LocalDateTime.now();
        Usuario usuario = buscarUsuarioExecutor(request.usuarioId(), pet);
        validarAtualizacao(status, usuario, request.prazo(), agora);

        Status novoStatus = statusService.findStatusByNome(status);
        LocalDateTime conclusao = definirConclusao(tarefaAtual, status, agora);
        return request.aplicarEm(tarefaAtual, pet, usuario, novoStatus, conclusao);
    }

    @Transactional
    public Tarefa concluir(Long id, TarefaConclusaoRequest request) {
        expirarTarefasPendentesAtrasadas();

        Tarefa tarefa = findTarefaById(id);
        if (!tarefa.estaPendente()) {
            throw new IllegalArgumentException("Apenas tarefas pendentes podem ser concluidas.");
        }

        Usuario usuario = findUsuarioById(request.concluinteId());
        validarCuidadorDoPet(usuario.getId(), tarefa.getPet().getId());

        tarefa.concluir(usuario, statusService.findStatusByNome(EnumStatus.CONCLUIDO), LocalDateTime.now());
        return tarefa;
    }

    @Transactional(readOnly = true)
    public Integer calcularPontosTotaisUsuario(Long usuarioId) {
        findUsuarioById(usuarioId);
        return tarefaRepository.calcularPontosTotaisUsuario(usuarioId, EnumStatus.CONCLUIDO);
    }

    @Transactional
    public void delete(Long id) {
        findTarefaById(id);
        tarefaRepository.deleteById(id);
    }

    private void validarCriacao(TarefaRequest request) {
        if (request.usuarioId() != null) {
            throw new IllegalArgumentException(
                    "Tarefa deve ser criada sem usuario executor. Use o endpoint de conclusao para registrar o cuidador.");
        }
    }

    private Usuario buscarUsuarioExecutor(Long usuarioId, Pet pet) {
        if (usuarioId == null) {
            return null;
        }

        Usuario usuario = findUsuarioById(usuarioId);
        validarCuidadorDoPet(usuario.getId(), pet.getId());
        return usuario;
    }

    private void validarAtualizacao(
            EnumStatus status,
            Usuario usuario,
            LocalDateTime prazo,
            LocalDateTime agora) {
        if (status == EnumStatus.EXPIRADO && prazo.isAfter(agora)) {
            throw new IllegalArgumentException("Nao e permitido marcar como EXPIRADO antes do vencimento do prazo.");
        }

        if (status != EnumStatus.EXPIRADO && prazo.isBefore(agora)) {
            throw new IllegalArgumentException("Prazo nao pode estar no passado para uma tarefa nao expirada.");
        }

        if (status == EnumStatus.CONCLUIDO && usuario == null) {
            throw new IllegalArgumentException("Uma tarefa concluida deve informar o usuario executor.");
        }

        if (status != EnumStatus.CONCLUIDO && usuario != null) {
            throw new IllegalArgumentException("Somente tarefas concluidas podem possuir usuario executor.");
        }
    }

    private LocalDateTime definirConclusao(Tarefa tarefa, EnumStatus status, LocalDateTime agora) {
        if (status == EnumStatus.CONCLUIDO) {
            return tarefa.getConclusao() == null ? agora : tarefa.getConclusao();
        }
        return null;
    }

    private void validarCuidadorDoPet(Long usuarioId, Long petId) {
        if (!usuarioPetRepository.existsByUsuarioIdAndPetId(usuarioId, petId)) {
            throw new IllegalArgumentException("Usuario informado nao esta vinculado ao pet da tarefa.");
        }
    }

    private Tarefa findTarefaById(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa com id " + id + " nao encontrada."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com id " + id + " nao encontrado."));
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com id " + id + " nao encontrado."));
    }

    private void expirarTarefasPendentesAtrasadas() {
        Status pendente = statusService.findStatusByNome(EnumStatus.PENDENTE);
        Status expirado = statusService.findStatusByNome(EnumStatus.EXPIRADO);
        tarefaRepository.expirarTarefasPendentesAtrasadas(LocalDateTime.now(), pendente, expirado);
    }
}
