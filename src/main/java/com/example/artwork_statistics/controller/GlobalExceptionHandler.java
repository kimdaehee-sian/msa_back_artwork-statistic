package com.example.artwork_statistics.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException e, HttpServletRequest request) {
        log.error("NoResourceFoundException: {} - Request URL: {} - Method: {}", 
                e.getMessage(), request.getRequestURL(), request.getMethod());
        log.error("Request URI: {}, Context Path: {}, Servlet Path: {}", 
                request.getRequestURI(), request.getContextPath(), request.getServletPath());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.NOT_FOUND.value(),
                        "error", "Not Found",
                        "message", "The requested resource was not found: " + request.getRequestURI(),
                        "path", request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error occurred - URL: {} - Method: {}", 
                request.getRequestURL(), request.getMethod(), e);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", "Internal Server Error",
                        "message", e.getMessage() != null ? e.getMessage() : "An unexpected error occurred",
                        "path", request.getRequestURI()
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("Runtime error occurred - URL: {} - Method: {}", 
                request.getRequestURL(), request.getMethod(), e);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "error", "Runtime Error",
                        "message", e.getMessage() != null ? e.getMessage() : "A runtime error occurred",
                        "path", request.getRequestURI()
                ));
    }
} 