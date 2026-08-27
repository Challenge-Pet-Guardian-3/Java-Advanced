package fiap.com.br.petguardian.auth;

import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioService;
import fiap.com.br.petguardian.usuario.dto.UsuarioResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public record LoginRequest(
            @NotBlank(message = "O e-mail e obrigatorio")
            @Email(message = "Formato de e-mail invalido")
            String email,

            @NotBlank(message = "A senha e obrigatoria")
            String senha
    ) {}

    public record LoginResponse(String token, UsuarioResponse user) {}

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        String token = tokenService.generateToken(auth.getName());
        Usuario usuario = usuarioService.findUsuarioByEmail(request.email());

        return new LoginResponse(token, UsuarioResponse.fromEntity(usuario));
    }
}
