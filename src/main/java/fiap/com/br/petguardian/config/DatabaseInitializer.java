package fiap.com.br.petguardian.config;

import fiap.com.br.petguardian.status.EnumStatus;
import fiap.com.br.petguardian.status.Status;
import fiap.com.br.petguardian.status.StatusRepository;
import fiap.com.br.petguardian.atendimento.tipoatendimento.EnumTipoAtendimento;
import fiap.com.br.petguardian.atendimento.tipoatendimento.TipoAtendimento;
import fiap.com.br.petguardian.atendimento.tipoatendimento.TipoAtendimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final StatusRepository statusRepository;
    private final TipoAtendimentoRepository tipoAtendimentoRepository;

    @Override
    public void run(String... args) {
        // Inicializar Status essenciais
        for (EnumStatus enumStatus : EnumStatus.values()) {
            if (statusRepository.findByNomeStatus(enumStatus).isEmpty()) {
                statusRepository.save(Status.builder().nome_status(enumStatus).build());
            }
        }

        // Inicializar Tipos de Atendimento essenciais
        for (EnumTipoAtendimento enumTipo : EnumTipoAtendimento.values()) {
            if (tipoAtendimentoRepository.findByTipoAtendimento(enumTipo).isEmpty()) {
                tipoAtendimentoRepository.save(TipoAtendimento.builder().tipoAtendimento(enumTipo).build());
            }
        }
    }
}
