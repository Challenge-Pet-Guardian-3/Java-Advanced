package fiap.com.br.petguardian;

import fiap.com.br.petguardian.auth.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;


@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableConfigurationProperties(SecurityConfig.RsaKeyProperties.class)
@ConfigurationPropertiesScan
public class PetGuardianApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetGuardianApplication.class, args);
    }

}
