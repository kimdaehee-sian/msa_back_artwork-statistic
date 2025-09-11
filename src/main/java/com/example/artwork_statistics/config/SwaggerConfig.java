package com.example.artwork_statistics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Artwork Statistics API")
                        .description("작품 통계 및 좋아요 관리 API")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Guidely Team")
                                .email("contact@guidely.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development"),
                        new Server().url("http://localhost:8082").description("Docker Environment"),
                        new Server().url("https://guidely-artwork-statistic-fah0b3dte6hvech2.koreacentral-01.azurewebsites.net").description("Azure Production")));
    }
} 