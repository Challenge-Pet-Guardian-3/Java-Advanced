package fiap.com.br.petguardian.veterinario;

import fiap.com.br.petguardian.veterinario.dto.VeterinarioRequest;
import fiap.com.br.petguardian.veterinario.dto.VeterinarioResponse;
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
@RequestMapping("/veterinarios")
@RequiredArgsConstructor
@Tag(name = "Veterinários", description = "Gerenciamento de médicos veterinários")
public class VeterinarioController {
    private final VeterinarioService veterinarioService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos os veterinários com paginação e ordenação")
    public Page<VeterinarioResponse> findAll(@PageableDefault(size = 10, page = 0, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return veterinarioService.findAll(pageable)
                .map(VeterinarioResponse::fromEntity);
    }

    @GetMapping("by-nome")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar veterinários por nome com paginação e ordenação")
    public Page<VeterinarioResponse> findByNome(@RequestParam String nome, @PageableDefault(size = 10, page = 0, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return veterinarioService.findByNome(nome, pageable)
                .map(VeterinarioResponse::fromEntity);
    }

    @GetMapping("by-email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar veterinário por e-mail")
    public VeterinarioResponse findByEmail(@RequestParam String email) {
        return VeterinarioResponse.fromEntity(veterinarioService.findVeterinarioByEmail(email));
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar veterinário por ID")
    public VeterinarioResponse findById(@PathVariable Long id) {
        return VeterinarioResponse.fromEntity(veterinarioService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar veterinário")
    public VeterinarioResponse create(@Valid @RequestBody VeterinarioRequest request) {
        return VeterinarioResponse.fromEntity(veterinarioService.create(request));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar veterinário")
    public VeterinarioResponse update(@PathVariable Long id, @Valid @RequestBody VeterinarioRequest request) {
        return VeterinarioResponse.fromEntity(veterinarioService.update(id, request));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar veterinário")
    public void delete(@PathVariable Long id) {
        veterinarioService.delete(id);
    }
}
