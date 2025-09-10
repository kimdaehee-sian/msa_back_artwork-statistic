package com.example.artwork_statistics.controller;

import com.example.artwork_statistics.dto.TopArtworksResponseDto;
import com.example.artwork_statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {
    
    private final StatisticsService statisticsService;
    
    @GetMapping("/top3")
    public ResponseEntity<TopArtworksResponseDto> getTop3Artworks() {
        log.info("Received request for top 3 artworks");
        
        TopArtworksResponseDto response = statisticsService.getTop3Artworks();
        
        log.info("Returning {} top artworks", response.getTopArtworks().size());
        
        return ResponseEntity.ok(response);
    }
} 