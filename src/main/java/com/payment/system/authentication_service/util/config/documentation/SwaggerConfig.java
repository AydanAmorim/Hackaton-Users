package com.payment.system.authentication_service.util.config.documentation;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
    @Value("${swagger.gateway.address}")
    private String gateway;

    String schemeName = "bearerAuth";

    String bearerFormat = "JWT";

    String scheme = "bearer";

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(schemeName)).components(new Components()
                        .addSecuritySchemes(
                                schemeName, new SecurityScheme()
                                        .name(schemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat(bearerFormat)
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme(scheme)
                        )
                ).addServersItem(new Server().url(gateway))
                .info(
                        new Info()
                            .title("API para criação de Usuários e Token JWT")
                            .description("Autenticação e criação de novos usuários")
                            .version("1.0.0")
                );
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("Usuário")
                .packagesToScan("com.payment.system.authentication_service.framework.web")
                .build();
    }
}
