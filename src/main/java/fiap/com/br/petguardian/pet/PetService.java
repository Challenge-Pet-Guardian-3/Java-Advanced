package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.atendimento.AtendimentoRepository;
import fiap.com.br.petguardian.atendimento.dto.AtendimentoResponse;
import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.pet.dto.PetHistoryResponse;
import fiap.com.br.petguardian.pet.dto.PetRequest;
import fiap.com.br.petguardian.pet.raca.Raca;
import fiap.com.br.petguardian.pet.raca.RacaRepository;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.tarefa.dto.TarefaResponse;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioPetRepository usuarioPetRepository;
    private final RacaRepository racaRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final TarefaRepository tarefaRepository;

    public Page<Pet> findAll(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    public Page<Pet> findByNome(String nome, Pageable pageable) {
        return petRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Pet findById(Long id) {
        return findPetById(id);
    }

    public Pet create(PetRequest petRequest) {
        Usuario usuario = findUsuarioById(petRequest.usuarioId());
        Raca raca = findOrCreateRaca(petRequest.raca());
        Pet petSalvo = petRepository.save(petRequest.toEntity(raca));

        usuarioPetRepository.save(UsuarioPet.principal(usuario, petSalvo));
        return petSalvo;
    }

    public Pet update(Long id, PetRequest petRequest) {
        Pet petAtual = findPetById(id);
        Usuario usuario = findUsuarioById(petRequest.usuarioId());
        Raca raca = findOrCreateRaca(petRequest.raca());

        Pet pet = petRequest.toEntity(raca);
        pet.setId(id);
        Pet petSalvo = petRepository.save(pet);

        usuarioPetRepository.limparResponsavelPrincipalPorPet(petAtual.getId());
        UsuarioPet usuarioPet = usuarioPetRepository.findByUsuarioIdAndPetId(usuario.getId(), petAtual.getId())
                .orElseGet(() -> UsuarioPet.of(usuario, petAtual, false));

        usuarioPet.setResponsavelPrincipal(Boolean.TRUE);
        usuarioPetRepository.save(usuarioPet);

        return petSalvo;
    }

    public void delete(Long id) {
        findPetById(id);
        usuarioPetRepository.deleteByPetId(id);
        petRepository.deleteById(id);
    }

    public void vincularUsuario(Long petId, Long usuarioId, boolean principal) {
        Pet pet = findPetById(petId);
        Usuario usuario = findUsuarioById(usuarioId);

        if (principal) {
            usuarioPetRepository.limparResponsavelPrincipalPorPet(petId);
        }

        UsuarioPet usuarioPet = usuarioPetRepository.findByUsuarioIdAndPetId(usuarioId, petId)
                .orElseGet(() -> UsuarioPet.of(usuario, pet, false));

        usuarioPet.setResponsavelPrincipal(principal);
        usuarioPetRepository.save(usuarioPet);
    }

    public void desvincularUsuario(Long petId, Long usuarioId) {
        UsuarioPet usuarioPet = usuarioPetRepository.findByUsuarioIdAndPetId(usuarioId, petId)
                .orElseThrow(() -> new ResourceNotFoundException("Vinculo nao encontrado entre o usuario e o pet informados."));

        if (Boolean.TRUE.equals(usuarioPet.getResponsavelPrincipal())) {
            throw new IllegalArgumentException("Nao e permitido desvincular o responsavel principal do pet.");
        }

        usuarioPetRepository.delete(usuarioPet);
    }

    public void vincularCuidadorPorResponsavelPrincipal(Long petId, Long responsavelPrincipalId, Long usuarioConvidadoId) {
        if (!usuarioPetRepository.isResponsavelPrincipal(responsavelPrincipalId, petId)) {
            throw new IllegalArgumentException("Somente o responsavel principal pode convidar co-cuidadores para este pet.");
        }

        if (usuarioPetRepository.existsByUsuarioIdAndPetId(usuarioConvidadoId, petId)) {
            throw new IllegalArgumentException("Usuario informado ja esta vinculado ao pet.");
        }

        Pet pet = findPetById(petId);
        Usuario usuarioConvidado = findUsuarioById(usuarioConvidadoId);
        usuarioPetRepository.save(UsuarioPet.of(usuarioConvidado, pet, false));
    }

    public void vincularCuidadorPorEmail(Long petId, Long responsavelPrincipalId, String emailConvidado) {
        Usuario usuarioConvidado = usuarioRepository.findByEmailIgnoreCase(emailConvidado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com email " + emailConvidado + " nao encontrado."));

        vincularCuidadorPorResponsavelPrincipal(petId, responsavelPrincipalId, usuarioConvidado.getId());
    }

    public PetHistoryResponse getConsolidatedHistory(Long petId) {
        Pet pet = findPetById(petId);

        var atendimentos = atendimentoRepository.findAllByPetIdOrderByDataDesc(petId)
                .stream()
                .map(AtendimentoResponse::fromEntity)
                .toList();

        var tarefasConcluidas = tarefaRepository.findConcluidasByPetId(petId)
                .stream()
                .map(TarefaResponse::fromEntity)
                .toList();

        return new PetHistoryResponse(
                pet.getId(),
                pet.getNome(),
                atendimentos,
                tarefasConcluidas
        );
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet com id " + id + " nao encontrado."));
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario com id " + id + " nao encontrado."));
    }

    private Raca findOrCreateRaca(String nomeRaca) {
        return racaRepository.findByNome(nomeRaca).orElseGet(() -> racaRepository.save(Raca.builder().nome(nomeRaca).build()));
    }
}
