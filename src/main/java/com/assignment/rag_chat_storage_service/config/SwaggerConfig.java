package com.assignment.rag_chat_storage_service.config;

import com.assignment.rag_chat_storage_service.constant.Constants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(Constants.TITLE)
                        .version(Constants.VERSION)
                        .description(Constants.DESCRIPTION))
                .addSecurityItem(new SecurityRequirement().addList(Constants.API_AUTH_KEY))
                .components(new Components()
                        .addSecuritySchemes(Constants.API_AUTH_KEY, new SecurityScheme()
                                .name(Constants.API_KEY_HEADER_NAME)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("API key required to access endpoints")));
    }
}
