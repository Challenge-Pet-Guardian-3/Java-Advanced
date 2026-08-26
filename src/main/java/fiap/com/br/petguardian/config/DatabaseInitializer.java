package fiap.com.br.petguardian.config;

import fiap.com.br.petguardian.status.EnumStatus;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final StatusRepository statusRepository;

    @Override
    public void run(String... args) {
        for (EnumStatus enumStatus : EnumStatus.values()) {
            if (statusRepository.findByNomeStatus(enumStatus).isEmpty()) {
                statusRepository.save(Status.builder().nomeStatus(enumStatus).build());
            }
        }
    }
}
