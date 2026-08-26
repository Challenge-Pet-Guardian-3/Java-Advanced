package fiap.com.br.petguardian.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "PetGuardian API",
                description = "API da plataforma de cuidado colaborativo centrada no Pet. Tutores compartilham responsabilidades, gerenciam tarefas da rotina do animal e acompanham o histórico consolidado de cuidados em um único lugar.",
                version = "1.0"
        ),
        tags = {
                @Tag(name = "Usuarios", description = "Gerenciamento de usuarios (tutores/cuidadores)"),
                @Tag(name = "Pets", description = "Gerenciamento de pets e rede de co-cuidadores"),
                @Tag(name = "Tarefas", description = "Gerenciamento de tarefas e rotinas de cuidados do pet"),
                @Tag(name = "Enderecos", description = "Gerenciamento de enderecos")
        }
)
public class SwaggerConfig {
}
