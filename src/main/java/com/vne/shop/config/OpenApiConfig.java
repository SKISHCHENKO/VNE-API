package com.vne.shop.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vneOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VNE Techwear Shop API")
                        .description("Базовый REST API для интернет-магазина VNE (techwear)")
                        .version("0.1.0")
                        .license(new License().name("MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("README"));
    }
}
