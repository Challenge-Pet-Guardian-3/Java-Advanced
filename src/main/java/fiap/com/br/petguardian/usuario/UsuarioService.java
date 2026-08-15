package fiap.com.br.petguardian.usuario;

import fiap.com.br.petguardian.atendimento.AtendimentoRepository;
import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.endereco.Endereco;
import fiap.com.br.petguardian.endereco.EnderecoService;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.telefone.Telefone;
import fiap.com.br.petguardian.telefone.TelefoneRepository;
import fiap.com.br.petguardian.usuario.dto.RedeCuidadoResponse;
import fiap.com.br.petguardian.usuario.dto.UsuarioRequest;
import fiap.com.br.petguardian.usuariopet.UsuarioPet;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final EnderecoService enderecoService;
    private final TelefoneRepository telefoneRepository;
    private final UsuarioPetRepository usuarioPetRepository;
    private final TarefaRepository tarefaRepository;
    private final AtendimentoRepository atendimentoRepository;

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
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(usuarioRequest.endereco());
        Telefone telefone = telefoneRepository.save(Telefone.builder().ddd(usuarioRequest.ddd()).numero(usuarioRequest.numeroTelefone()).build());
        Usuario usuario = usuarioRequest.toEntity(telefone);
        usuario.getEnderecos().add(endereco);
        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, UsuarioRequest usuarioRequest) {
        findUsuarioById(id);
        Endereco endereco = enderecoService.findOrCreateByCepAndNumero(usuarioRequest.endereco());
        Telefone telefone = telefoneRepository.save(Telefone.builder().ddd(usuarioRequest.ddd()).numero(usuarioRequest.numeroTelefone()).build());
        Usuario usuario = usuarioRequest.toEntity(telefone);
        usuario.setId(id);
        usuario.getEnderecos().add(endereco);
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        findUsuarioById(id);
        usuarioRepository.deleteById(id);
    }

    public RedeCuidadoResponse getRedeCuidado(Long usuarioId) {
        Usuario usuario = findUsuarioById(usuarioId);

        // 1. Buscar todos os pets vinculados a este usuario
        List<UsuarioPet> meusVinculos = usuarioPetRepository.findAllByUsuarioId(usuarioId);
        List<Long> petIds = meusVinculos.stream().map(up -> up.getPet().getId()).toList();

        if (petIds.isEmpty()) {
            return new RedeCuidadoResponse(
                    usuarioId, usuario.getNome(),
                    List.of(), List.of(),
                    0, 0, 0, 0
            );
        }

        // 2. Para cada pet, montar PetResumo com IDs de tarefas e atendimentos
        List<RedeCuidadoResponse.PetResumo> petResumos = new ArrayList<>();
        for (UsuarioPet vinculo : meusVinculos) {
            Long petId = vinculo.getPet().getId();
            List<Long> tarefaIds = tarefaRepository.findIdsByPetId(petId);
            List<Long> atendimentoIds = atendimentoRepository.findIdsByPetId(petId);

            petResumos.add(new RedeCuidadoResponse.PetResumo(
                    petId,
                    vinculo.getPet().getNome(),
                    vinculo.getPet().getRaca() != null ? vinculo.getPet().getRaca().getNome() : null,
                    Boolean.TRUE.equals(vinculo.getResponsavelPrincipal()),
                    tarefaIds,
                    atendimentoIds
            ));
        }

        // 3. Buscar todos os co-cuidadores desses pets (excluindo o proprio usuario)
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

        // 4. Totalizadores
        int totalPendentes = tarefaRepository.countPendentesByPetIdIn(petIds);
        int totalConcluidas = tarefaRepository.countConcluidasByPetIdIn(petIds);
        int totalAtendimentos = atendimentoRepository.countByPetIdIn(petIds);
        int pontos = tarefaRepository.calcularPontosTotaisUsuario(usuarioId);

        return new RedeCuidadoResponse(
                usuarioId,
                usuario.getNome(),
                petResumos,
                coCuidadores,
                totalPendentes,
                totalConcluidas,
                totalAtendimentos,
                pontos
        );
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario com id " + id + " nao encontrado." ));
    }

    public Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com email " + email + " nao encontrado." ));
    }

    // Helper interno para montar co-cuidadores agrupados
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
