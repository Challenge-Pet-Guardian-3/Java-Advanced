package fiap.com.br.petguardian.pet;

import fiap.com.br.petguardian.familia.FamiliaMembro;
import fiap.com.br.petguardian.familia.FamiliaMembroRepository;
import fiap.com.br.petguardian.usuariopet.UsuarioPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Centraliza a pergunta "esse usuário pode gerenciar/cuidar desse pet?" —
// usada tanto por Tarefa quanto por outros fluxos que envolvem permissão
// de cuidador. Antes vivia duplicada dentro do TarefaService.
@Service
@RequiredArgsConstructor
public class PetPermissaoService {

    private final UsuarioPetRepository usuarioPetRepository;
    private final FamiliaMembroRepository familiaMembroRepository;

    public boolean ehCuidadorDoPet(Long usuarioId, Long petId) {
        if (usuarioPetRepository.existsByUsuarioIdAndPetId(usuarioId, petId)) {
            return true;
        }

        Optional<FamiliaMembro> membroLogado = familiaMembroRepository.findByUsuarioId(usuarioId);
        if (membroLogado.isPresent() && membroLogado.get().getFamilia() != null) {
            Long familiaId = membroLogado.get().getFamilia().getId();
            List<FamiliaMembro> todosMembros = familiaMembroRepository.findByFamiliaId(familiaId);

            return todosMembros.stream()
                    .filter(m -> m.getUsuario() != null)
                    .map(m -> m.getUsuario().getId())
                    .anyMatch(idMembro -> usuarioPetRepository.existsByUsuarioIdAndPetId(idMembro, petId));
        }

        return false;
    }

    public void validarCuidadorDoPet(Long usuarioId, Long petId) {
        if (!ehCuidadorDoPet(usuarioId, petId)) {
            throw new IllegalArgumentException("Você não possui permissão para gerenciar as tarefas deste pet.");
        }
    }
}