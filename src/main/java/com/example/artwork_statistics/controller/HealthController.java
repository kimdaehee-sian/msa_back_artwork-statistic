package com.example.artwork_statistics.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class HealthController {
    
    @Autowired(required = false)
    private DataSource dataSource;
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.info("Health check requested");
        
        boolean dbHealthy = checkDatabaseHealth();
        
        return ResponseEntity.ok(Map.of(
                "status", dbHealthy ? "UP" : "DOWN",
                "timestamp", LocalDateTime.now(),
                "database", dbHealthy ? "Connected" : "Disconnected",
                "service", "artwork-statistics"
        ));
    }
    
    private boolean checkDatabaseHealth() {
        if (dataSource == null) {
            log.warn("DataSource is null");
            return false;
        }
        
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5);
        } catch (Exception e) {
            log.error("Database health check failed", e);
            return false;
        }
    }
} 