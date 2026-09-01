package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.pet.dto.PetRequest;
import fiap.com.br.petguardian.pet.dto.PetResponse;
import fiap.com.br.petguardian.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Gerenciamento de pets e rede de co-cuidadores")
public class PetController {
    private final PetService petService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar pets do usuario autenticado")
    public Page<PetResponse> findAll(
            Authentication authentication,
            @PageableDefault(size = 10, page = 0, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        return petService.findAllByUsuario(usuarioLogado.getId(), pageable)
                .map(PetResponse::fromEntity);
    }

    @GetMapping("/by-familia")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar pets de todos os membros da familia do usuario autenticado")
    public Page<PetResponse> findAllByFamilia(
            Authentication authentication,
            @PageableDefault(size = 10, page = 0, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        return petService.findAllByFamilia(usuarioLogado.getId(), pageable)
                .map(PetResponse::fromEntity);
    }

    @GetMapping("by-nome")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar pets por nome com paginacao e ordenacao")
    public Page<PetResponse> findByNome(@RequestParam String nome, @PageableDefault(size = 10, page = 0, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return petService.findByNome(nome, pageable)
                .map(PetResponse::fromEntity);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar pet por ID")
    public PetResponse findById(@PathVariable Long id) {
        return PetResponse.fromEntity(petService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar pet")
    public PetResponse create(Authentication authentication, @Valid @RequestBody PetRequest petRequest) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        return PetResponse.fromEntity(petService.create(petRequest, usuarioLogado.getId()));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualizar pet")
    public PetResponse update(Authentication authentication, @PathVariable Long id, @Valid @RequestBody PetRequest petRequest) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        return PetResponse.fromEntity(petService.update(id, petRequest, usuarioLogado.getId()));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar pet")
    public void delete(Authentication authentication, @PathVariable Long id) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        petService.delete(id, usuarioLogado.getId());
    }

    @PostMapping("{id}/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Vincular um usuario a um pet (somente responsavel principal)")
    public void vincularUsuario(Authentication authentication, @PathVariable Long id, @PathVariable Long usuarioId, @RequestParam(defaultValue = "false") boolean principal) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        petService.vincularUsuario(id, usuarioId, principal, usuarioLogado.getId());
    }

    @DeleteMapping("{id}/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desvincular um usuario de um pet (o proprio usuario ou o responsavel principal)")
    public void desvincularUsuario(Authentication authentication, @PathVariable Long id, @PathVariable Long usuarioId) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        petService.desvincularUsuario(id, usuarioId, usuarioLogado.getId());
    }

    @PostMapping("{id}/convidar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Convidar co-cuidador por ID (somente responsavel principal)")
    public void convidarCuidadorPorId(Authentication authentication, @PathVariable Long id, @RequestParam Long usuarioConvidadoId) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        petService.vincularCuidadorPorResponsavelPrincipal(id, usuarioLogado.getId(), usuarioConvidadoId);
    }

    @PostMapping("{id}/convidar-email")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Convidar co-cuidador por e-mail (somente responsavel principal)")
    public void convidarCuidadorPorEmail(Authentication authentication, @PathVariable Long id, @RequestParam String email) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        petService.vincularCuidadorPorEmail(id, usuarioLogado.getId(), email);
    }
}