package fiap.com.br.petguardian.usuario;

import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.EnderecoService;
import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.telefone.TelefoneRepository;
import fiap.com.br.petguardian.usuario.dto.RedeCuidadoResponse;
import fiap.com.br.petguardian.usuario.dto.UsuarioRequest;
import fiap.com.br.petguardian.usuariopet.UsuarioPetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EnderecoService enderecoService;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioPetService usuarioPetService;

    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Page<Usuario> findByNome(String nome, Pageable pageable) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Usuario findById(Long id) {
        return findUsuarioById(id);
    }

    @Transactional
    public Usuario create(UsuarioRequest usuarioRequest) {
        return salvarUsuarioComRelacionamentos(null, usuarioRequest);
    }

    @Transactional
    public Usuario update(Long id, UsuarioRequest usuarioRequest) {
        findUsuarioById(id);
        return salvarUsuarioComRelacionamentos(id, usuarioRequest);
    }

    public void delete(Long id) {
        findUsuarioById(id);
        usuarioRepository.deleteById(id);
    }

    public RedeCuidadoResponse getRedeCuidado(Long usuarioId) {
        return usuarioPetService.montarRedeCuidado(usuarioId);
    }

    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com email " + email + " nao encontrado."));
    }

    private Usuario salvarUsuarioComRelacionamentos(Long id, UsuarioRequest request) {
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(request.endereco());
        Telefone telefone = telefoneRepository.save(request.toTelefone());

        Usuario usuario = request.toEntity(telefone);
        usuario.setId(id);
        usuario.getEnderecos().add(endereco);
        return usuarioRepository.save(usuario);
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com id " + id + " nao encontrado."));
    }
}
