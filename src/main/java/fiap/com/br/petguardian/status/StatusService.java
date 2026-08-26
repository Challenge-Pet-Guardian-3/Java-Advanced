package fiap.com.br.petguardian.status;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    public Page<Status> findAll(Pageable pageable) {
        return statusRepository.findAll(pageable);
    }

    public Status findById(Long id) {
        return findStatusById(id);
    }

    @Cacheable(value = "status", key = "#nome.toUpperCase()")
    public Status findStatusByNome(String nome) {
        return statusRepository.findByNomeStatus(EnumStatus.valueOf(nome.toUpperCase())).orElseThrow(() -> new ResourceNotFoundException("Status '" + nome + "' não encontrado."));
    }

    private Status findStatusById(Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Status com id " + id + " não encontrado."));
    }
}
