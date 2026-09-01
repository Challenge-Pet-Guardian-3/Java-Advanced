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
import fiap.com.br.petguardian.validation.TarefaValidator;
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
    private final StatusService statusService;
    private final TarefaValidator tarefaValidator;

    public Page<Tarefa> findAll(Pageable pageable) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findAll(pageable);
    }

    public Page<Tarefa> findAll(Long usuarioId, Pageable pageable) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findTarefasPendentesDoCuidador(usuarioId, EnumStatus.PENDENTE, pageable);
    }

    public Tarefa findById(Long id) {
        expirarTarefasPendentesAtrasadas();
        return findTarefaById(id);
    }

    public Tarefa findByUsuarioIdAndTarefaId(Long usuarioId, Long tarefaId) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findByIdAndUsuarioId(tarefaId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa com id " + tarefaId + " nao encontrada para o usuario informado."));
    }

    @Transactional
    public Tarefa create(TarefaRequest request) {
        LocalDateTime agora = LocalDateTime.now();
        Pet pet = findPetById(request.petId());
        Usuario usuario = findUsuarioById(request.usuarioId());
        tarefaValidator.validarCuidadorDoPet(usuario.getId(), pet.getId());
        Status statusPendente = statusService.findStatusByNome(EnumStatus.PENDENTE.name());

        Tarefa tarefa = request.toEntity(usuario, pet, agora);
        tarefa.setStatus(statusPendente);
        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public Tarefa update(Long id, TarefaRequest request) {
        Tarefa tarefaAtual = findTarefaById(id);
        Pet pet = findPetById(request.petId());
        Usuario usuario = findUsuarioById(request.usuarioId());
        tarefaValidator.validarCuidadorDoPet(usuario.getId(), pet.getId());
        EnumStatus status = EnumStatus.valueOf(request.status().trim().toUpperCase());
        LocalDateTime agora = LocalDateTime.now();

        Status novoStatus = statusService.findStatusByNome(status.name());
        LocalDateTime conclusao = definirConclusao(tarefaAtual, status, agora);

        Tarefa tarefa = request.toEntity(usuario, pet, tarefaAtual.getCriacao());
        tarefa.setId(id);
        tarefa.setStatus(novoStatus);
        tarefa.setConclusao(conclusao);
        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public Tarefa concluir(Long id, TarefaConclusaoRequest request) {
        expirarTarefasPendentesAtrasadas();

        Tarefa tarefa = findTarefaById(id);
        tarefaValidator.validarPendenteParaConclusao(tarefa);

        Usuario usuario = findUsuarioById(request.concluinteId());
        tarefaValidator.validarCuidadorDoPet(usuario.getId(), tarefa.getPet().getId());

        tarefa.setUsuario(usuario);
        tarefa.setStatus(statusService.findStatusByNome(EnumStatus.CONCLUIDO.name()));
        tarefa.setConclusao(LocalDateTime.now());
        return tarefaRepository.save(tarefa);
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

    private LocalDateTime definirConclusao(Tarefa tarefa, EnumStatus status, LocalDateTime agora) {
        if (status == EnumStatus.CONCLUIDO) {
            return tarefa.getConclusao() == null ? agora : tarefa.getConclusao();
        }
        return null;
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
        Status pendente = statusService.findStatusByNome(EnumStatus.PENDENTE.name());
        Status expirado = statusService.findStatusByNome(EnumStatus.EXPIRADO.name());
        tarefaRepository.expirarTarefasPendentesAtrasadas(LocalDateTime.now(), pendente, expirado);
    }
}
