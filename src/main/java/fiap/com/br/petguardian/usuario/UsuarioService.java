package fiap.com.br.petguardian.usuario;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.usuario.dto.RedeCuidadoResponse;
import fiap.com.br.petguardian.usuario.dto.UsuarioRequest;
import fiap.com.br.petguardian.usuario.dto.UsuarioUpdateRequest;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioPetRepository usuarioPetRepository;
    private final TarefaRepository tarefaRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Page<Usuario> findByNome(String nome, Pageable pageable) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public Usuario findById(Long id) {
        return findUsuarioById(id);
    }

    public Usuario create(UsuarioRequest usuarioRequest) {
        Usuario usuario = usuarioRequest.toEntity();
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Long id, UsuarioUpdateRequest usuarioUpdateRequest) {
        Usuario usuarioExistente = findUsuarioById(id);

        usuarioExistente.setNome(usuarioUpdateRequest.nome().trim());
        usuarioExistente.setEmail(usuarioUpdateRequest.email().trim().toLowerCase());
        usuarioExistente.setTelefone(usuarioUpdateRequest.telefone());

        if (usuarioUpdateRequest.senha() != null && !usuarioUpdateRequest.senha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioUpdateRequest.senha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void delete(Long id) {
        findUsuarioById(id);
        usuarioRepository.deleteById(id);
    }

    public RedeCuidadoResponse getRedeCuidado(Long usuarioId) {
        Usuario usuario = findUsuarioById(usuarioId);

        List<UsuarioPet> meusVinculos = usuarioPetRepository.findAllByUsuarioId(usuarioId);
        List<Long> petIds = meusVinculos.stream().map(up -> up.getPet().getId()).toList();

        if (petIds.isEmpty()) {
            return new RedeCuidadoResponse(
                    usuarioId, usuario.getNome(),
                    List.of(), List.of(),
                    0, 0, 0
            );
        }

        List<RedeCuidadoResponse.PetResumo> petResumos = new ArrayList<>();
        for (UsuarioPet vinculo : meusVinculos) {
            Long petId = vinculo.getPet().getId();
            List<Long> tarefaIds = tarefaRepository.findIdsByPetId(petId);

            petResumos.add(new RedeCuidadoResponse.PetResumo(
                    petId,
                    vinculo.getPet().getNome(),
                    vinculo.getPet().getRaca() != null ? vinculo.getPet().getRaca().getNome() : null,
                    Boolean.TRUE.equals(vinculo.getResponsavelPrincipal()),
                    tarefaIds
            ));
        }

        List<UsuarioPet> todosVinculos = usuarioPetRepository.findAllByPetIdIn(petIds);
        Map<Long, CuidadorBuilder> cuidadorMap = new LinkedHashMap<>();

        for (UsuarioPet vinculo : todosVinculos) {
            Long cuidadorId = vinculo.getUsuario().getId();
            if (cuidadorId.equals(usuarioId)) continue;

            cuidadorMap.computeIfAbsent(cuidadorId, k -> new CuidadorBuilder(
                    vinculo.getUsuario().getNome(),
                    vinculo.getUsuario().getEmail()
            ));

            CuidadorBuilder cb = cuidadorMap.get(cuidadorId);
            cb.petIds.add(vinculo.getPet().getId());
            if (Boolean.TRUE.equals(vinculo.getResponsavelPrincipal())) {
                cb.responsavelPrincipal = true;
            }
        }

        List<RedeCuidadoResponse.CuidadorResumo> coCuidadores = cuidadorMap.entrySet().stream()
                .map(e -> new RedeCuidadoResponse.CuidadorResumo(
                        e.getKey(),
                        e.getValue().nome,
                        e.getValue().email,
                        e.getValue().responsavelPrincipal,
                        e.getValue().petIds
                ))
                .toList();

        int totalPendentes = tarefaRepository.countPendentesByPetIdIn(petIds);
        int totalConcluidas = tarefaRepository.countConcluidasByPetIdIn(petIds);
        int pontos = tarefaRepository.calcularPontosTotaisUsuario(usuarioId);

        return new RedeCuidadoResponse(
                usuarioId,
                usuario.getNome(),
                petResumos,
                coCuidadores,
                totalPendentes,
                totalConcluidas,
                pontos
        );
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com id " + id + " nao encontrado."));
    }

    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com email " + email + " nao encontrado."));
    }

    private static class CuidadorBuilder {
        String nome;
        String email;
        boolean responsavelPrincipal = false;
        List<Long> petIds = new ArrayList<>();

        CuidadorBuilder(String nome, String email) {
            this.nome = nome;
            this.email = email;
        }
    }
}