package com.example.shemtong.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(
                        new Info().title("Shemtong API")
                                .version("1.0.1")
                )
                .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(
                        new Components()
                                .addSecuritySchemes("Authorization",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("Authorization")
                                                .in(SecurityScheme.In.HEADER)
                                                .name("Authorization")
                                ));
    }
}
