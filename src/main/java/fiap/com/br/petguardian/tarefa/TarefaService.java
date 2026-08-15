package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusService;
import fiap.com.br.petguardian.tarefa.dto.TarefaConclusaoRequest;
import fiap.com.br.petguardian.tarefa.dto.TarefaRequest;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import fiap.com.br.petguardian.veterinario.Veterinario;
import fiap.com.br.petguardian.veterinario.VeterinarioService;
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
    private final VeterinarioService veterinarioService;
    private final StatusService statusService;

    public Page<Tarefa> findAll(Pageable pageable) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findAll(pageable);
    }

    public Page<Tarefa> findAll(Long usuarioId, Pageable pageable) {
        expirarTarefasPendentesAtrasadas();
        return tarefaRepository.findTarefasPendentesDoCuidador(usuarioId, pageable);
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

    public Tarefa create(TarefaRequest request) {
        if (request.usuarioId() != null) {
            throw new IllegalArgumentException("Tarefa deve ser criada sem usuario executor. Use o endpoint de conclusao para registrar o cuidador.");
        }

        Pet pet = findPetById(request.petId());
        Veterinario veterinario = veterinarioService.findById(request.veterinarioId());
 
        Tarefa tarefa = request.toEntity(null, pet, veterinario);
        tarefa.setStatus(statusService.findStatusByNome("PENDENTE"));
        tarefa.setConclusao(null);
        return tarefaRepository.save(tarefa);
    }
 
    @Transactional
    public Tarefa update(Long id, TarefaRequest request) {
        Tarefa tarefaAtual = findTarefaById(id);
        Pet pet = findPetById(request.petId());
        Veterinario veterinario = veterinarioService.findById(request.veterinarioId());

        Usuario usuario = null;
        if (request.usuarioId() != null) {
            usuario = findUsuarioById(request.usuarioId());
            validarCuidadorDoPet(usuario.getId(), pet.getId());
        }

        Tarefa tarefa = request.toEntity(usuario, pet, veterinario);
        tarefa.setId(tarefaAtual.getId());
        tarefa.setCriacao(tarefaAtual.getCriacao());

        String statusStr = request.status();
        if ("EXPIRADO".equalsIgnoreCase(statusStr) && request.prazo().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Nao e permitido marcar como EXPIRADO antes do vencimento do prazo.");
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
    public Tarefa concluir(Long id, TarefaConclusaoRequest request) {
        expirarTarefasPendentesAtrasadas();

        Tarefa tarefa = findTarefaById(id);
        if (!"PENDENTE".equals(tarefa.getStatus().getNome_status().name())) {
            throw new IllegalArgumentException("Apenas tarefas pendentes podem ser concluidas.");
        }

        Usuario usuario = findUsuarioById(request.concluinteId());
        validarCuidadorDoPet(usuario.getId(), tarefa.getPet().getId());

        if (tarefa.getUsuario() != null && !tarefa.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Tarefa ja possui cuidador associado.");
        }

        tarefa.setUsuario(usuario);
        tarefa.setStatus(statusService.findStatusByNome("CONCLUIDO"));
        tarefa.setConclusao(LocalDateTime.now());
        return tarefaRepository.save(tarefa);
    }

    public Integer calcularPontosTotaisUsuario(Long usuarioId) {
        findUsuarioById(usuarioId);
        return tarefaRepository.calcularPontosTotaisUsuario(usuarioId);
    }

    public void delete(Long id) {
        findTarefaById(id);
        tarefaRepository.deleteById(id);
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
        Status pendente = statusService.findStatusByNome("PENDENTE");
        Status expirado = statusService.findStatusByNome("EXPIRADO");
        tarefaRepository.expirarTarefasPendentesAtrasadas(LocalDateTime.now(), pendente, expirado);
    }
}
