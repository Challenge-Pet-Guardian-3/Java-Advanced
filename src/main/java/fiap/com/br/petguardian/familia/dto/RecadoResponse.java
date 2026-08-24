package fiap.com.br.petguardian.familia.dto;

import fiap.com.br.petguardian.familia.Recado;
import java.time.LocalDateTime;

public record RecadoResponse(Long id, String texto, String autor, LocalDateTime dataHora, Boolean editado) {
    public static RecadoResponse fromEntity(Recado r) {
        return new RecadoResponse(r.getId(), r.getTexto(), r.getAutor().getNome(), r.getDataHora(), r.getEditado());
    }
}