package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.familia.dto.*;
import fiap.com.br.petguardian.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/familia")
@RequiredArgsConstructor
@Tag(name = "Familia", description = "Gerenciamento da familia (Matilha) e mural de recados")
public class FamiliaController {
    private final FamiliaService familiaService;

    @GetMapping
    @Operation(summary = "Obter a familia do usuario autenticado (200 vazio se ele ainda nao tiver uma)")
    public ResponseEntity<FamiliaResponse> getMinhaFamilia(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return familiaService.buscarFamiliaOpcional(usuario.getId())
                .map(familia -> ResponseEntity.ok(FamiliaResponse.fromEntity(familia)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar uma nova familia")
    public FamiliaResponse criar(Authentication authentication, @Valid @RequestBody CriarFamiliaRequest request) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return FamiliaResponse.fromEntity(familiaService.criarFamilia(usuario, request.nome()));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Renomear a familia (somente dono)")
    public FamiliaResponse renomear(Authentication authentication, @Valid @RequestBody RenomearFamiliaRequest request) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return FamiliaResponse.fromEntity(familiaService.renomearFamilia(usuario.getId(), request.nome()));
    }

    @PostMapping("/entrar")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Entrar em uma familia existente usando o codigo de convite")
    public FamiliaResponse entrar(Authentication authentication, @Valid @RequestBody EntrarFamiliaRequest request) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return FamiliaResponse.fromEntity(familiaService.entrarFamilia(usuario, request.codigo(), request.funcao()));
    }

    @PostMapping("/sair")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Sair da familia atual")
    public void sair(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        familiaService.sairFamilia(usuario.getId());
    }

    @DeleteMapping("/membros/{membroId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover um membro (somente dono)")
    public void removerMembro(Authentication authentication, @PathVariable Long membroId) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        familiaService.removerMembro(usuario.getId(), membroId);
    }

    @GetMapping("/recados")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar recados do mural da familia")
    public List<RecadoResponse> listarRecados(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return FamiliaResponse.fromEntity(familiaService.getFamiliaDoUsuario(usuario.getId())).recados();
    }

    @PostMapping("/recados")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar um recado ao mural")
    public RecadoResponse adicionarRecado(Authentication authentication, @Valid @RequestBody RecadoRequest request) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return RecadoResponse.fromEntity(familiaService.adicionarRecado(usuario.getId(), request.texto()));
    }

    @PutMapping("/recados/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Editar um recado (somente autor)")
    public RecadoResponse editarRecado(Authentication authentication, @PathVariable Long id, @Valid @RequestBody RecadoRequest request) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return RecadoResponse.fromEntity(familiaService.editarRecado(usuario.getId(), id, request.texto()));
    }

    @DeleteMapping("/recados/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir um recado (autor ou dono)")
    public void excluirRecado(Authentication authentication, @PathVariable Long id) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        familiaService.excluirRecado(usuario.getId(), id);
    }
}