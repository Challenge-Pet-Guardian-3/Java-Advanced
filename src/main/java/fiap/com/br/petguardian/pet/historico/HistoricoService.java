package fiap.com.br.petguardian.pet.historico;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.pet.historico.dto.HistoricoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoRepository historicoRepository;
    private final PetRepository petRepository;

    @Transactional
    public Historico create(HistoricoRequest request) {
        Pet pet = findPetById(request.petId());
        return historicoRepository.save(request.toEntity(pet));
    }

    public List<Historico> findAllByPetId(Long petId) {
        findPetById(petId);
        return historicoRepository.findAllByPetIdOrderByDataHistDesc(petId);
    }

    public Historico findById(Long id) {
        return historicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historico com id " + id + " nao encontrado."));
    }

    @Transactional
    public Historico update(Long id, HistoricoRequest request) {
        findById(id);
        Pet pet = findPetById(request.petId());
        Historico historico = request.toEntity(pet);
        historico.setId(id);
        return historicoRepository.save(historico);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        historicoRepository.deleteById(id);
    }

    private Pet findPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com id " + petId + " nao encontrado."));
    }
}
