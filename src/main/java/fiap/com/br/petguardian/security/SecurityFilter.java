package fiap.com.br.petguardian.security;

import fiap.com.br.petguardian.familia.FamiliaMembroRepository;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FamiliaMembroRepository familiaMembroRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null && !tokenJWT.isBlank() && !tokenJWT.equals("null") && !tokenJWT.equals("undefined")) {
            try {
                var subject = tokenService.getSubject(tokenJWT);
                if (subject != null) {
                    Long usuarioId = Long.parseLong(subject);
                    Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

                    if (usuario != null) {
                        boolean ehDonoDeFamilia = familiaMembroRepository.findByUsuarioId(usuario.getId())
                                .map(membro -> Boolean.TRUE.equals(membro.getResponsavelPrincipal()))
                                .orElse(false);
                        usuario.setResponsavelPrincipalFamilia(ehDonoDeFamilia);

                        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            var token = authorizationHeader.replace("Bearer ", "").trim();
            return token.isEmpty() ? null : token;
        }
        return null;
    }
}