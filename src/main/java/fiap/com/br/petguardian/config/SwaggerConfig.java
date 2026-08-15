package fiap.com.br.petguardian.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "PetGuardian API",
                description = "API da plataforma de cuidado colaborativo centrada no Pet. Tutores compartilham responsabilidades, veterinarios prescrevem tarefas gamificadas e o historico clinico do pet e consolidado em um unico lugar.",
                version = "1.0"
        ),
        tags = {
                @Tag(name = "Usuarios", description = "Gerenciamento de usuarios (tutores/cuidadores)"),
                @Tag(name = "Pets", description = "Gerenciamento de pets e rede de co-cuidadores"),
                @Tag(name = "Tarefas", description = "Tarefas gamificadas prescritas por veterinarios"),
                @Tag(name = "Atendimentos", description = "Atendimentos clinicos veterinarios"),
                @Tag(name = "Clinicas", description = "Gerenciamento de clinicas veterinarias"),
                @Tag(name = "Veterinarios", description = "Gerenciamento de medicos veterinarios"),
                @Tag(name = "Enderecos", description = "Gerenciamento de enderecos")
        }
)
public class SwaggerConfig {
}
