package com.example.artwork_statistics.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external-services.exhibition-artwork")
@Getter
@Setter
public class ExternalServiceConfig {
    private String baseUrl;
    private int timeout;
} 