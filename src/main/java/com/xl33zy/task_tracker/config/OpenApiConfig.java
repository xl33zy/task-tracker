package com.xl33zy.task_tracker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Tracker API")
                        .version("1.0")
                        .description("REST API for managing tasks")
                        .contact(new Contact().name("Baubek Zhumabayev").email("baubekzhumabayev@gmail.com"))

                )
                .servers(List.of(new Server().url("http:localhost:8080").description("Local server")))
                .components(new Components());
    }
}
