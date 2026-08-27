package fiap.com.br.petguardian.usuariopet;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.pet.Pet;
import fiap.com.br.petguardian.pet.PetRepository;
import fiap.com.br.petguardian.tarefa.TarefaRepository;
import fiap.com.br.petguardian.usuario.Usuario;
import fiap.com.br.petguardian.usuario.UsuarioRepository;
import fiap.com.br.petguardian.usuario.dto.RedeCuidadoResponse;
import fiap.com.br.petguardian.usuariopet.dto.CoCuidadorRequest;
import fiap.com.br.petguardian.usuariopet.dto.CoCuidadorResponse;
import fiap.com.br.petguardian.usuariopet.dto.TransferirResponsabilidadeRequest;
import fiap.com.br.petguardian.validation.UsuarioPetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioPetService {

    private final UsuarioPetRepository usuarioPetRepository;
    private final UsuarioRepository usuarioRepository;
    private final PetRepository petRepository;
    private final TarefaRepository tarefaRepository;
    private final UsuarioPetValidator usuarioPetValidator;
    private final RedeCuidadoMapper redeCuidadoMapper;

    @Transactional
    public UsuarioPet vincularResponsavelPrincipal(Usuario usuario, Pet pet) {
        usuarioPetRepository.limparResponsavelPrincipalPorPet(pet.getId());
        UsuarioPet vinculo = usuarioPetRepository.findByUsuarioIdAndPetId(usuario.getId(), pet.getId())
                .orElseGet(() -> new UsuarioPet(new UsuarioPetId(usuario.getId(), pet.getId()), usuario, pet, true));
        vinculo.promoverResponsavelPrincipal();
        return usuarioPetRepository.save(vinculo);
    }

    @Transactional
    public CoCuidadorResponse convidarCoCuidador(Long petId, CoCuidadorRequest request) {
        Pet pet = findPetById(petId);
        Usuario convidado = findUsuarioByEmail(request.email());

        usuarioPetValidator.validarResponsavelPrincipal(request.responsavelPrincipalId(), petId);
        usuarioPetValidator.validarUsuarioNaoVinculado(convidado.getId(), petId);

        UsuarioPet novoVinculo = usuarioPetRepository.save(request.toEntity(convidado, pet));
        return CoCuidadorResponse.fromEntity(novoVinculo);
    }

    @Transactional
    public void desvincularCuidador(Long petId, Long usuarioId, Long solicitanteId) {
        UsuarioPet vinculo = findVinculo(usuarioId, petId);
        usuarioPetValidator.validarPermissaoDesvinculacao(vinculo, solicitanteId);
        usuarioPetRepository.delete(vinculo);
    }

    @Transactional
    public void transferirResponsabilidadePrincipal(Long petId, TransferirResponsabilidadeRequest request) {
        usuarioPetValidator.validarResponsavelPrincipal(request.responsavelAtualId(), petId);

        UsuarioPet novoResponsavel = findVinculo(request.novoResponsavelId(), petId);
        usuarioPetRepository.limparResponsavelPrincipalPorPet(petId);
        novoResponsavel.promoverResponsavelPrincipal();
        usuarioPetRepository.save(novoResponsavel);
    }

    @Transactional(readOnly = true)
    public List<CoCuidadorResponse> listarCuidadoresDoPet(Long petId) {
        findPetById(petId);
        return usuarioPetRepository.findAllByPetId(petId)
                .stream()
                .map(CoCuidadorResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public RedeCuidadoResponse montarRedeCuidado(Long usuarioId) {
        Usuario usuario = findUsuarioById(usuarioId);
        List<UsuarioPet> meusVinculos = usuarioPetRepository.findAllByUsuarioId(usuarioId);

        if (meusVinculos.isEmpty()) {
            return redeCuidadoMapper.toEmptyResponse(usuario);
        }

        List<Long> petIds = meusVinculos.stream()
                .map(up -> up.getPet().getId())
                .toList();

        Map<Long, List<Long>> tarefasPorPet = carregarMapaTarefasPorPet(petIds);
        List<UsuarioPet> todosVinculosDosPets = usuarioPetRepository.findAllByPetIdIn(petIds);

        int totalPendentes = tarefaRepository.countPendentesByPetIdIn(petIds);
        int totalConcluidas = tarefaRepository.countConcluidasByPetIdIn(petIds);
        int pontosTotais = tarefaRepository.calcularPontosTotaisUsuario(usuarioId);

        var petResumos = redeCuidadoMapper.toPetResumoList(meusVinculos, tarefasPorPet);
        var coCuidadores = redeCuidadoMapper.toCuidadorResumoList(todosVinculosDosPets, usuarioId);

        return redeCuidadoMapper.toResponse(
                usuario,
                petResumos,
                coCuidadores,
                totalPendentes,
                totalConcluidas,
                pontosTotais
        );
    }

    private Map<Long, List<Long>> carregarMapaTarefasPorPet(List<Long> petIds) {
        Map<Long, List<Long>> mapa = new HashMap<>();
        petIds.forEach(id -> mapa.put(id, new ArrayList<>()));

        List<Object[]> registros = tarefaRepository.findTarefaIdsByPetIdIn(petIds);
        for (Object[] row : registros) {
            Long petId = (Long) row[0];
            Long tarefaId = (Long) row[1];
            mapa.get(petId).add(tarefaId);
        }
        return mapa;
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet com id " + id + " nao encontrado."));
    }

    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com id " + id + " nao encontrado."));
    }

    private Usuario findUsuarioByEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario com email " + email + " nao encontrado."));
    }

    private UsuarioPet findVinculo(Long usuarioId, Long petId) {
        return usuarioPetRepository.findByUsuarioIdAndPetId(usuarioId, petId)
                .orElseThrow(() -> new ResourceNotFoundException("Vinculo nao encontrado entre o usuario e o pet informados."));
    }
}
