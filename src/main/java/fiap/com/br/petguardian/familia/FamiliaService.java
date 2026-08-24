package fiap.com.br.petguardian.familia;

import fiap.com.br.petguardian.exception.ResourceNotFoundException;
import fiap.com.br.petguardian.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FamiliaService {

    private static final int XP_DONO_INICIAL = 30;
    private static final int XP_MEMBRO_INICIAL = 15;

    private final FamiliaRepository familiaRepository;
    private final FamiliaMembroRepository membroRepository;
    private final RecadoRepository recadoRepository;

    public Familia criarFamilia(Usuario dono, String nome) {
        if (membroRepository.findByUsuarioId(dono.getId()).isPresent()) {
            throw new IllegalArgumentException("Usuario ja pertence a uma familia.");
        }

        Familia familia = Familia.builder()
                .nome(nome.trim())
                .codigoConvite(gerarCodigoUnico())
                .dataCriacao(LocalDateTime.now())
                .build();

        Familia familiaSalva = familiaRepository.save(familia);

        FamiliaMembro donoMembro = FamiliaMembro.builder()
                .familia(familiaSalva)
                .usuario(dono)
                .funcao("Dono(a) da Familia")
                .xp(XP_DONO_INICIAL)
                .responsavelPrincipal(true)
                .dataEntrada(LocalDateTime.now())
                .build();

        membroRepository.save(donoMembro);
        return familiaSalva;
    }

    public Familia entrarFamilia(Usuario usuario, String codigo, String funcao) {
        if (membroRepository.findByUsuarioId(usuario.getId()).isPresent()) {
            throw new IllegalArgumentException("Usuario ja pertence a uma familia.");
        }

        Familia familia = familiaRepository.findByCodigoConvite(codigo.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Codigo de convite invalido."));

        FamiliaMembro membro = FamiliaMembro.builder()
                .familia(familia)
                .usuario(usuario)
                .funcao(funcao != null && !funcao.isBlank() ? funcao.trim() : "Co-cuidador")
                .xp(XP_MEMBRO_INICIAL)
                .responsavelPrincipal(false)
                .dataEntrada(LocalDateTime.now())
                .build();

        membroRepository.save(membro);
        return familia;
    }

    public Familia getFamiliaDoUsuario(Long usuarioId) {
        return membroRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao pertence a nenhuma familia."))
                .getFamilia();
    }

    public void sairFamilia(Long usuarioId) {
        FamiliaMembro membro = membroRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao pertence a nenhuma familia."));

        if (Boolean.TRUE.equals(membro.getResponsavelPrincipal())) {
            long total = membroRepository.findByFamiliaId(membro.getFamilia().getId()).size();
            if (total > 1) {
                throw new IllegalArgumentException("O dono nao pode sair enquanto houver outros membros na familia.");
            }
            familiaRepository.delete(membro.getFamilia());
            return;
        }

        membroRepository.delete(membro);
    }

    public void removerMembro(Long solicitanteId, Long membroId) {
        FamiliaMembro solicitante = membroRepository.findByUsuarioId(solicitanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao pertence a nenhuma familia."));

        if (!Boolean.TRUE.equals(solicitante.getResponsavelPrincipal())) {
            throw new IllegalArgumentException("Somente o dono da familia pode remover membros.");
        }

        FamiliaMembro membro = membroRepository.findById(membroId)
                .orElseThrow(() -> new ResourceNotFoundException("Membro nao encontrado."));

        if (!membro.getFamilia().getId().equals(solicitante.getFamilia().getId())) {
            throw new IllegalArgumentException("Membro nao pertence a esta familia.");
        }
        if (Boolean.TRUE.equals(membro.getResponsavelPrincipal())) {
            throw new IllegalArgumentException("Nao e possivel remover o dono da familia.");
        }

        membroRepository.delete(membro);
    }

    public Familia renomearFamilia(Long usuarioId, String novoNome) {
        FamiliaMembro membro = membroRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao pertence a nenhuma familia."));

        if (!Boolean.TRUE.equals(membro.getResponsavelPrincipal())) {
            throw new IllegalArgumentException("Somente o dono da familia pode renomea-la.");
        }

        Familia familia = membro.getFamilia();
        familia.setNome(novoNome.trim());
        return familiaRepository.save(familia);
    }

    public Recado adicionarRecado(Long usuarioId, String texto) {
        FamiliaMembro membro = membroRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao pertence a nenhuma familia."));

        Recado recado = Recado.builder()
                .familia(membro.getFamilia())
                .autor(membro.getUsuario())
                .texto(texto.trim())
                .dataHora(LocalDateTime.now())
                .editado(false)
                .build();

        return recadoRepository.save(recado);
    }

    public Recado editarRecado(Long usuarioId, Long recadoId, String novoTexto) {
        FamiliaMembro membro = membroRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao pertence a nenhuma familia."));

        Recado recado = recadoRepository.findById(recadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Recado nao encontrado."));

        if (!recado.getFamilia().getId().equals(membro.getFamilia().getId())) {
            throw new IllegalArgumentException("Recado nao pertence a esta familia.");
        }
        if (!recado.getAutor().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Somente o autor pode editar o recado.");
        }

        recado.setTexto(novoTexto.trim());
        recado.setEditado(true);
        return recadoRepository.save(recado);
    }

    public void excluirRecado(Long usuarioId, Long recadoId) {
        FamiliaMembro membro = membroRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao pertence a nenhuma familia."));

        Recado recado = recadoRepository.findById(recadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Recado nao encontrado."));

        if (!recado.getFamilia().getId().equals(membro.getFamilia().getId())) {
            throw new IllegalArgumentException("Recado nao pertence a esta familia.");
        }

        boolean ehAutor = recado.getAutor().getId().equals(usuarioId);
        boolean ehDono = Boolean.TRUE.equals(membro.getResponsavelPrincipal());
        if (!ehAutor && !ehDono) {
            throw new IllegalArgumentException("Somente o autor ou o dono podem excluir o recado.");
        }

        recadoRepository.delete(recado);
    }

    private String gerarCodigoUnico() {
        String codigo;
        do {
            codigo = "PET-" + (1000 + new Random().nextInt(9000));
        } while (familiaRepository.existsByCodigoConvite(codigo));
        return codigo;
    }
}