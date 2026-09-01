// src/main/java/fiap/com/br/petguardian/ia/IaMensagemRepository.java
package fiap.com.br.petguardian.ia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IaMensagemRepository extends JpaRepository<IaMensagem, Long> {

    List<IaMensagem> findByUsuarioIdOrderByDataHoraAsc(Long usuarioId);
}