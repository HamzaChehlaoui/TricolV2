package com.tricol.tricolV2.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tricol API Documentation")
                        .description("Documentation de l'API de gestion des fournisseurs et des commandes")
                        .version("2.0.0"));
    }
}
