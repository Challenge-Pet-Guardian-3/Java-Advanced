package fiap.com.br.petguardian.atendimento;

import fiap.com.br.petguardian.atendimento.dto.AtendimentoRequest;
import fiap.com.br.petguardian.atendimento.dto.AtendimentoResponse;
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
@RequestMapping("/atendimentos")
@RequiredArgsConstructor
@Tag(name = "Atendimentos", description = "Gerenciamento de atendimentos veterinários")
public class AtendimentoController {
    private final AtendimentoService atendimentoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar todos os atendimentos com paginacao e ordenacao")
    public Page<AtendimentoResponse> findAll(@PageableDefault(size = 10, page = 0, sort = "data", direction = Sort.Direction.DESC) Pageable pageable) {
        return atendimentoService.findAll(pageable)
                .map(AtendimentoResponse::fromEntity);
    }

    @GetMapping("by-usuario")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar atendimentos por usuarioId com paginacao e ordenacao")
    public Page<AtendimentoResponse> findAllByUsuario(@RequestParam Long usuarioId, @PageableDefault(size = 10, page = 0, sort = "data", direction = Sort.Direction.DESC) Pageable pageable) {
        return atendimentoService.findAllByUsuario(usuarioId, pageable)
                .map(AtendimentoResponse::fromEntity);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar atendimento por ID")
    public AtendimentoResponse findById(@PathVariable Long id) {
        return AtendimentoResponse.fromEntity(atendimentoService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar atendimento")
    public AtendimentoResponse create(@Valid @RequestBody AtendimentoRequest atendimentoRequest) {
        return AtendimentoResponse.fromEntity(atendimentoService.create(atendimentoRequest));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar atendimento")
    public AtendimentoResponse update(@PathVariable Long id, @Valid @RequestBody AtendimentoRequest atendimentoRequest) {
        return AtendimentoResponse.fromEntity(atendimentoService.update(id, atendimentoRequest));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar atendimento")
    public void delete(@PathVariable Long id) {
        atendimentoService.delete(id);
    }
}
