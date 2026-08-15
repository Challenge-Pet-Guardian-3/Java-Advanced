package fiap.com.br.petguardian.atendimento;

import fiap.com.br.petguardian.veterinario.Veterinario;
import fiap.com.br.petguardian.veterinario.VeterinarioService;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusService;
import fiap.com.br.petguardian.atendimento.tipoatendimento.TipoAtendimento;
import fiap.com.br.petguardian.atendimento.tipoatendimento.TipoAtendimentoService;

import fiap.com.br.petguardian.atendimento.dto.AtendimentoRequest;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AtendimentoService {
    private final AtendimentoRepository atendimentoRepository;
    private final PetRepository petRepository;
    private final VeterinarioService veterinarioService;
    private final StatusService statusService;
    private final TipoAtendimentoService tipoAtendimentoService;

    public Page<Atendimento> findAll(Pageable pageable) {
        expirarAtendimentosPendentesAtrasados();
        return atendimentoRepository.findAll(pageable);
    }

    public Page<Atendimento> findAllByUsuario(Long usuarioId, Pageable pageable) {
        expirarAtendimentosPendentesAtrasados();
        return atendimentoRepository.findAllByUsuarioId(usuarioId, pageable);
    }

    public Atendimento findById(Long id) {
        expirarAtendimentosPendentesAtrasados();
        return findAtendimentoById(id);
    }

    public Atendimento create(AtendimentoRequest atendimentoRequest) {
        Pet pet = findPetById(atendimentoRequest.petId());
        Veterinario veterinario = veterinarioService.findById(atendimentoRequest.veterinarioId());
 
        TipoAtendimento tipoObj = tipoAtendimentoService.findTipoAtendimentoByNome(atendimentoRequest.tipo());
        Status statusObj = statusService.findStatusByNome("PENDENTE");
        Atendimento atendimento = atendimentoRequest.toEntity(tipoObj, pet, veterinario, statusObj);
        return atendimentoRepository.save(atendimento);
    }
 
    public Atendimento update(Long id, AtendimentoRequest atendimentoRequest) {
        findAtendimentoById(id);
        expirarAtendimentosPendentesAtrasados();
 
        Pet pet = findPetById(atendimentoRequest.petId());
        Veterinario veterinario = veterinarioService.findById(atendimentoRequest.veterinarioId());

        String statusStr = atendimentoRequest.status();

        if ("EXPIRADO".equalsIgnoreCase(statusStr) && atendimentoRequest.data().isAfter(LocalDateTime.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é permitido marcar como EXPIRADO antes da data do atendimento.");

        TipoAtendimento tipoObj = tipoAtendimentoService.findTipoAtendimentoByNome(atendimentoRequest.tipo());
        Status statusObj = statusService.findStatusByNome(statusStr);
        Atendimento atendimento = atendimentoRequest.toEntity(tipoObj, pet, veterinario, statusObj);
        atendimento.setId(id);
        return atendimentoRepository.save(atendimento);
    }

    public void delete(Long id) {
        findAtendimentoById(id);
        atendimentoRepository.deleteById(id);
    }

    private Atendimento findAtendimentoById(Long id) {
        return atendimentoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atendimento com id " + id + " não encontrado."));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet com id " + id + " não encontrado."));
    }

    private void expirarAtendimentosPendentesAtrasados() {
        Status pendente = statusService.findStatusByNome("PENDENTE");
        Status expirado = statusService.findStatusByNome("EXPIRADO");
        atendimentoRepository.expirarAtendimentosPendentesAtrasados(LocalDateTime.now(), pendente, expirado);
    }
}
