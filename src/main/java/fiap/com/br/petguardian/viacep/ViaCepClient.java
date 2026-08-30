package fiap.com.br.petguardian.viacep;

import fiap.com.br.petguardian.viacep.dto.ViaCepResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ViaCepClient {

    private final RestClient restClient = RestClient.create("https://viacep.com.br/ws");

    public ViaCepResponse buscar(String cep) {
        ViaCepResponse resposta = restClient.get()
                .uri("/{cep}/json/", cep)
                .retrieve()
                .body(ViaCepResponse.class);

        if (resposta == null || resposta.erro()) {
            throw new IllegalArgumentException("CEP nao encontrado: " + cep);
        }
        return resposta;
    }
}