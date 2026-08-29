package fiap.com.br.petguardian.trilha.aula;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.trilha.aula.dto.AulaRequest;
import fiap.com.br.petguardian.trilha.modulo.Modulo;
import fiap.com.br.petguardian.trilha.modulo.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AulaService {

    private final AulaRepository aulaRepository;
    private final ModuloRepository moduloRepository;

    @Transactional
    public Aula create(AulaRequest request) {
        Modulo modulo = findModuloById(request.moduloId());
        return aulaRepository.save(request.toEntity(modulo));
    }

    public List<Aula> findAllByModuloId(Long moduloId) {
        findModuloById(moduloId);
        return aulaRepository.findAllByModuloId(moduloId);
    }

    public Aula findById(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula com id " + id + " nao encontrada."));
    }

    @Transactional
    public Aula update(Long id, AulaRequest request) {
        findById(id);
        Modulo modulo = findModuloById(request.moduloId());
        Aula aula = request.toEntity(modulo);
        aula.setId(id);
        return aulaRepository.save(aula);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        aulaRepository.deleteById(id);
    }

    private Modulo findModuloById(Long moduloId) {
        return moduloRepository.findById(moduloId)
                .orElseThrow(() -> new ResourceNotFoundException("Modulo com id " + moduloId + " nao encontrado."));
    }
}
