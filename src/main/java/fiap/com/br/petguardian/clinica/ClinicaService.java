// src/main/java/fiap/com/br/petguardian/clinica/ClinicaService.java
package fiap.com.br.petguardian.clinica;

import fiap.com.br.petguardian.clinica.dto.ClinicaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicaService {

    private final ClinicaRepository clinicaRepository;

    public List<ClinicaResponse> buscar(String termo, Boolean atendimento24h, Boolean prontoSocorro) {
        return clinicaRepository.buscar(termo, atendimento24h, prontoSocorro)
                .stream()
                .map(ClinicaResponse::fromEntity)
                .toList();
    }
}