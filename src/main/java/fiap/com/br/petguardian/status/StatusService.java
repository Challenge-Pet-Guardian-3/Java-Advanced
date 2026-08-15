package fiap.com.br.petguardian.status;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
        return statusRepository.findByNomeStatus(EnumStatus.valueOf(nome.toUpperCase())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status '" + nome + "' não encontrado."));
    }

    private Status findStatusById(Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status com id " + id + " não encontrado."));
    }
}
