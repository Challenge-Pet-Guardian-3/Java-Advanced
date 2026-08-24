package fiap.com.br.petguardian.tarefa;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.familia.FamiliaMembro;
import fiap.com.br.petguardian.familia.FamiliaMembroRepository;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusService;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;
    private final UsuarioPetRepository usuarioPetRepository;
    private final FamiliaMembroRepository familiaMembroRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa com id " + tarefaId + " não encontrada para o usuário informado."));
    }

    @Transactional
    public Tarefa create(TarefaRequest request) {
        Pet pet = findPetById(request.petId());

        Tarefa tarefa = request.toEntity(null, pet);

        // Garante o preenchimento de campos obrigatórios
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
    public Tarefa update(Long id, TarefaRequest request) {
        Tarefa tarefaAtual = findTarefaById(id);
        Pet pet = findPetById(request.petId());

        Usuario usuario = null;
        if (request.usuarioId() != null) {
            usuario = findUsuarioById(request.usuarioId());
            validarPermissaoCuidador(usuario.getId(), pet.getId());
        }

        Tarefa tarefa = request.toEntity(usuario, pet);
        tarefa.setId(tarefaAtual.getId());
        tarefa.setCriacao(tarefaAtual.getCriacao() != null ? tarefaAtual.getCriacao() : LocalDateTime.now());

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
    public Tarefa concluir(Long id, TarefaConclusaoRequest request) {
        expirarTarefasPendentesAtrasadas();

        Tarefa tarefa = findTarefaById(id);
        if (!"PENDENTE".equalsIgnoreCase(tarefa.getStatus().getNome_status().name())) {
            throw new IllegalArgumentException("Apenas tarefas pendentes podem ser concluídas.");
        }

        Usuario usuario = findUsuarioById(request.concluinteId());
        validarPermissaoCuidador(usuario.getId(), tarefa.getPet().getId());

        tarefa.setUsuario(usuario);
        tarefa.setStatus(statusService.findStatusByNome("CONCLUIDO"));
        tarefa.setConclusao(LocalDateTime.now());

        // Incrementa o XP do membro na família com segurança
        try {
            familiaMembroRepository.findByUsuarioId(usuario.getId()).ifPresent(membro -> {
                int pontos = tarefa.getPontosTarefa() != null ? tarefa.getPontosTarefa() : 15;
                membro.setXp((membro.getXp() != null ? membro.getXp() : 0) + pontos);
                familiaMembroRepository.save(membro);
            });
        } catch (Exception ignored) {}

        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public Tarefa reabrir(Long id, Long solicitanteId) {
        Tarefa tarefa = findTarefaById(id);

        if (!"CONCLUIDO".equalsIgnoreCase(tarefa.getStatus().getNome_status().name())) {
            throw new IllegalArgumentException("Apenas tarefas concluídas podem ser reabertas.");
        }

        // Se solicitante for informado, impede que outro cuidador reabra a tarefa de outrem
        if (tarefa.getUsuario() != null && solicitanteId != null) {
            if (!tarefa.getUsuario().getId().equals(solicitanteId)) {
                throw new IllegalArgumentException("Você não pode desmarcar uma tarefa realizada por outro cuidador.");
            }

            try {
                familiaMembroRepository.findByUsuarioId(tarefa.getUsuario().getId()).ifPresent(membro -> {
                    int pontos = tarefa.getPontosTarefa() != null ? tarefa.getPontosTarefa() : 15;
                    int xpAtual = membro.getXp() != null ? membro.getXp() : 0;
                    membro.setXp(Math.max(0, xpAtual - pontos));
                    familiaMembroRepository.save(membro);
                });
            } catch (Exception ignored) {}
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

        if (solicitanteId != null) {
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
                try {
                    familiaMembroRepository.findByUsuarioId(tarefa.getUsuario().getId()).ifPresent(membro -> {
                        int pontos = tarefa.getPontosTarefa() != null ? tarefa.getPontosTarefa() : 15;
                        int xpAtual = membro.getXp() != null ? membro.getXp() : 0;
                        membro.setXp(Math.max(0, xpAtual - pontos));
                        familiaMembroRepository.save(membro);
                    });
                } catch (Exception ignored) {}
            }
        }

        tarefaRepository.delete(tarefa);
    }

    private void validarPermissaoCuidador(Long usuarioId, Long petId) {
        if (usuarioPetRepository.existsByUsuarioIdAndPetId(usuarioId, petId)) {
            return;
        }

        Optional<FamiliaMembro> membroLogado = familiaMembroRepository.findByUsuarioId(usuarioId);
        if (membroLogado.isPresent() && membroLogado.get().getFamilia() != null) {
            Long familiaId = membroLogado.get().getFamilia().getId();
            List<FamiliaMembro> todosMembros = familiaMembroRepository.findByFamiliaId(familiaId);

            boolean membroCuidaDoPet = todosMembros.stream()
                    .filter(m -> m.getUsuario() != null)
                    .map(m -> m.getUsuario().getId())
                    .anyMatch(idMembro -> usuarioPetRepository.existsByUsuarioIdAndPetId(idMembro, petId));

            if (membroCuidaDoPet) {
                return;
            }
        }

        throw new IllegalArgumentException("Você não possui permissão para gerenciar as tarefas deste pet.");
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