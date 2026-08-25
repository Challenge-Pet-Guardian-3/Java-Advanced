package fiap.com.br.petguardian.security;

import fiap.com.br.petguardian.familia.FamiliaMembroRepository;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final FamiliaMembroRepository familiaMembroRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        boolean ehDonoDeFamilia = familiaMembroRepository.findByUsuarioId(usuario.getId())
                .map(membro -> Boolean.TRUE.equals(membro.getResponsavelPrincipal()))
                .orElse(false);

        usuario.setResponsavelPrincipalFamilia(ehDonoDeFamilia);
        return usuario;
    }
}