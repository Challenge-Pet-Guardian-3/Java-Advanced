package fiap.com.br.petguardian.endereco;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Optional<Endereco> findByCepAndNumeroAndBairroId(String cep, String numero, Long bairroId);
}
