package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.pet.dto.PetHistoryResponse;
import fiap.com.br.petguardian.pet.dto.PetPontuacaoResponse;
import fiap.com.br.petguardian.pet.dto.PetRequest;
import fiap.com.br.petguardian.pet.raca.Raca;
import fiap.com.br.petguardian.pet.raca.RacaRepository;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.tarefa.status.EnumStatus;
import fiap.com.br.petguardian.tarefa.dto.TarefaResponse;
import fiap.com.br.petguardian.trilha.aula.AulaRepository;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.usuariopet.UsuarioPetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioPetService usuarioPetService;
    private final RacaRepository racaRepository;
    private final TarefaRepository tarefaRepository;
    private final AulaRepository aulaRepository;

    public Page<Pet> findAll(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    public Page<Pet> findByNome(String nome, Pageable pageable) {
        return petRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Pet findById(Long id) {
        return findPetById(id);
    }

    @Transactional
    public Pet create(PetRequest petRequest) {
        Usuario usuario = findUsuarioById(petRequest.usuarioId());
        Raca raca = findOrCreateRaca(petRequest.raca());
        Pet petSalvo = petRepository.save(petRequest.toEntity(raca));

        usuarioPetService.vincularResponsavelPrincipal(usuario, petSalvo);
        return petSalvo;
    }

    @Transactional
    public Pet update(Long id, PetRequest petRequest) {
        findPetById(id);
        Usuario usuario = findUsuarioById(petRequest.usuarioId());
        Raca raca = findOrCreateRaca(petRequest.raca());

        Pet pet = petRequest.toEntity(raca);
        pet.setId(id);
        Pet petSalvo = petRepository.save(pet);

        usuarioPetService.vincularResponsavelPrincipal(usuario, petSalvo);
        return petSalvo;
    }

    @Transactional
    public void delete(Long id) {
        findPetById(id);
        petRepository.deleteById(id);
    }

    public PetHistoryResponse getConsolidatedHistory(Long petId) {
        Pet pet = findPetById(petId);

        var tarefasConcluidas = tarefaRepository.findConcluidasByPetId(petId, EnumStatus.CONCLUIDO)
                .stream()
                .map(TarefaResponse::fromEntity)
                .toList();

        return new PetHistoryResponse(
                pet.getId(),
                pet.getNome(),
                tarefasConcluidas);
    }

    @Transactional(readOnly = true)
    public PetPontuacaoResponse calcularPontuacaoTotalPet(Long petId) {
        Pet pet = findPetById(petId);
        int pontosTarefas = tarefaRepository.calcularPontosTarefasPorPet(petId, EnumStatus.CONCLUIDO);
        int pontosAulas = aulaRepository.calcularPontosAulasConcluidasPorPet(petId);
        int pontosTotais = pontosTarefas + pontosAulas;

        return new PetPontuacaoResponse(pet.getId(), pet.getNome(), pontosTarefas, pontosAulas, pontosTotais);
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com id " + id + " nao encontrado."));
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com id " + id + " nao encontrado."));
    }

    private Raca findOrCreateRaca(String nomeRaca) {
        String nomeNormalizado = nomeRaca.trim();
        return racaRepository.findByNomeIgnoreCase(nomeNormalizado)
                .orElseGet(() -> racaRepository.save(Raca.builder().nome(nomeNormalizado).build()));
    }
}
