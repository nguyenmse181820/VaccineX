package com.sba301.vaccinex.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("VaccineX - Hệ thống Theo dõi Lịch tiêm chủng Trẻ em"))
                .addServersItem(
                        new Server().url("https://api.vaccinex.theanh0804.duckdns.org")
                )
                .addServersItem(
                        new Server().url("http://localhost:8080")
                )
                .addSecurityItem(new SecurityRequirement().addList("Dịch vụ Xác thực VaccineX"))

                .components(new Components().addSecuritySchemes("Dịch vụ Xác thực VaccineX", new SecurityScheme()
                        .name("VaccineX - Hệ thống Theo dõi Lịch tiêm chủng Trẻ em").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}