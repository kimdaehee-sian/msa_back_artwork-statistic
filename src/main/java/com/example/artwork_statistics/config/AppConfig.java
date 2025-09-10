package com.example.artwork_statistics.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@EnableJpaAuditing
public class AppConfig {
    
    @Bean
    public RestTemplate restTemplate(ExternalServiceConfig config) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(config.getTimeout()))
                .setReadTimeout(Duration.ofMillis(config.getTimeout()))
                .build();
    }
} 