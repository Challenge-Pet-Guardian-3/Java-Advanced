package fiap.com.br.petguardian.veterinario;

import fiap.com.br.petguardian.clinica.Clinica;
import fiap.com.br.petguardian.clinica.ClinicaService;
import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.telefone.TelefoneRepository;
import fiap.com.br.petguardian.veterinario.dto.VeterinarioRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeterinarioService {
    private final VeterinarioRepository veterinarioRepository;
    private final ClinicaService clinicaService;
    private final TelefoneRepository telefoneRepository;

    public Page<Veterinario> findAll(Pageable pageable) {
        return veterinarioRepository.findAll(pageable);
    }

    public Page<Veterinario> findByNome(String nome, Pageable pageable) {
        return veterinarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Veterinario findVeterinarioByEmail(String email) {
        return veterinarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinario com email " + email + " nao encontrado."));
    }

    public Veterinario findById(Long id) {
        return findVeterinarioById(id);
    }

    public Veterinario create(VeterinarioRequest request) {
        Clinica clinica = request.clinicaId() == null ? null : clinicaService.findById(request.clinicaId());
        Telefone telefone = telefoneRepository.save(Telefone.builder().ddd(request.ddd()).numero(request.numeroTelefone()).build());
        Veterinario veterinario = request.toEntity(telefone, clinica);
        return veterinarioRepository.save(veterinario);
    }

    public Veterinario update(Long id, VeterinarioRequest request) {
        findVeterinarioById(id);
        Clinica clinica = request.clinicaId() == null ? null : clinicaService.findById(request.clinicaId());
        Telefone telefone = telefoneRepository.save(Telefone.builder().ddd(request.ddd()).numero(request.numeroTelefone()).build());
        Veterinario veterinario = request.toEntity(telefone, clinica);
        veterinario.setId(id);
        return veterinarioRepository.save(veterinario);
    }

    public void delete(Long id) {
        findVeterinarioById(id);
        veterinarioRepository.deleteById(id);
    }

    private Veterinario findVeterinarioById(Long id) {
        return veterinarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Veterinario com id " + id + " nao encontrado."));
    }
}
