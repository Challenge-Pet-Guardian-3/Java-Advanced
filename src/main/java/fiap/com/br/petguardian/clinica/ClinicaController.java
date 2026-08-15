package fiap.com.br.petguardian.clinica;

import fiap.com.br.petguardian.clinica.dto.ClinicaRequest;
import fiap.com.br.petguardian.clinica.dto.ClinicaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clinicas")
@RequiredArgsConstructor
@Tag(name = "Clinicas", description = "Gerenciamento de clínicas veterinárias")
public class ClinicaController {
    private final ClinicaService clinicaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todas as clinicas com paginacao e ordenacao")
    public Page<ClinicaResponse> findAll(@PageableDefault(size = 10, page = 0, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return clinicaService.findAll(pageable)
                .map(ClinicaResponse::fromEntity);
    }

    @GetMapping("by-nome")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar clinicas por nome com paginacao e ordenacao")
    public Page<ClinicaResponse> findByNome(@RequestParam String nome, @PageableDefault(size = 10, page = 0, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return clinicaService.findByNome(nome, pageable)
                .map(ClinicaResponse::fromEntity);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar clínica por ID")
    public ClinicaResponse findById(@PathVariable Long id) {
        return ClinicaResponse.fromEntity(clinicaService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar clínica")
    public ClinicaResponse create(@Valid @RequestBody ClinicaRequest clinicaRequest) {
        return ClinicaResponse.fromEntity(clinicaService.create(clinicaRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar clínica")
    public ClinicaResponse update(@PathVariable Long id, @Valid @RequestBody ClinicaRequest clinicaRequest) {
        return ClinicaResponse.fromEntity(clinicaService.update(id, clinicaRequest));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar clínica")
    public void delete(@PathVariable Long id) {
        clinicaService.delete(id);
    }
}
