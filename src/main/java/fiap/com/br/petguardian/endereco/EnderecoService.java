package fiap.com.br.petguardian.endereco;

import fiap.com.br.petguardian.endereco.dto.EnderecoRequest;
import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;

    public Endereco findByUsuarioId(Long usuarioId) {
        return enderecoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereco nao encontrado para o usuario informado."));
    }

    public Endereco salvarOuAtualizar(Long usuarioId, EnderecoRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com id " + usuarioId + " nao encontrado."));

        Endereco endereco = enderecoRepository.findByUsuarioId(usuarioId)
                .orElse(Endereco.builder().usuario(usuario).build());

        endereco.setLogradouro(request.logradouro().trim());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());
        endereco.setBairro(request.bairro());
        endereco.setCidade(request.cidade().trim());
        endereco.setEstado(request.estado().trim().toUpperCase());
        endereco.setCep(request.cep().trim());

        return enderecoRepository.save(endereco);
    }

    public void delete(Long usuarioId) {
        Endereco endereco = findByUsuarioId(usuarioId);
        enderecoRepository.delete(endereco);
    }
}