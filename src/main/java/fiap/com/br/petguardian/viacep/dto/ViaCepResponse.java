package fiap.com.br.petguardian.viacep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade, // cidade
        String uf,         // estado
        @JsonProperty(defaultValue = "false") boolean erro
) {}