// src/main/java/fiap/com/br/petguardian/clinica/ClinicaController.java
package fiap.com.br.petguardian.clinica;

import fiap.com.br.petguardian.clinica.dto.ClinicaResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinicas")
@RequiredArgsConstructor
@Tag(name = "Clínicas", description = "Busca de clínicas veterinárias")
public class ClinicaController {

    private final ClinicaService clinicaService;

    @GetMapping
    public List<ClinicaResponse> buscar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) Boolean atendimento24h,
            @RequestParam(required = false) Boolean prontoSocorro
    ) {
        return clinicaService.buscar(busca, atendimento24h, prontoSocorro);
    }
}