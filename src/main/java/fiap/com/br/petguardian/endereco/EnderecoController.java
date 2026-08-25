package fiap.com.br.petguardian.endereco;

import fiap.com.br.petguardian.endereco.dto.EnderecoRequest;
import fiap.com.br.petguardian.endereco.dto.EnderecoResponse;
import fiap.com.br.petguardian.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
@Tag(name = "Enderecos", description = "Gerenciamento de enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar o endereco do usuario autenticado")
    public EnderecoResponse buscarMeuEndereco(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return EnderecoResponse.fromEntity(enderecoService.findByUsuarioId(usuario.getId()));
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Criar ou atualizar o endereco do usuario autenticado")
    public EnderecoResponse salvarMeuEndereco(Authentication authentication, @Valid @RequestBody EnderecoRequest request) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return EnderecoResponse.fromEntity(enderecoService.salvarOuAtualizar(usuario.getId(), request));
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir o endereco do usuario autenticado")
    public void excluirMeuEndereco(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        enderecoService.delete(usuario.getId());
    }
}