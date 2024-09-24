package com.pilot.project.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public static final String SCHEME_NAME="bearerScheme";

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")))
                .info(new Info()
                        .title("Developer Pilot Project")
                        .description("Developer Pilot Project")
                        .contact(new Contact().url("https://github.com/ashish-1609/DeveloperPilotProject").name("Ashish Mishra").email("ashish@celerispay.com"))
                );
    }
}

