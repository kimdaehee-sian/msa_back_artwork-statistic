package com.example.artwork_statistics.client;

import com.example.artwork_statistics.config.ExternalServiceConfig;
import com.example.artwork_statistics.dto.ArtworkDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExhibitionArtworkClient {
    
    private final RestTemplate restTemplate;
    private final ExternalServiceConfig config;
    
    @PostConstruct
    public void logConfig() {
        log.info("Exhibition Artwork Service configured with baseUrl: {}", config.getBaseUrl());
    }
    
    public ArtworkDetailDto getArtworkDetail(Long artworkId) {
        try {
            String url = config.getBaseUrl() + "/api/artworks/" + artworkId;
            log.debug("Calling Exhibition Artwork Service: {}", url);
            
            // RestTemplate null 체크
            if (restTemplate == null) {
                log.error("RestTemplate is null!");
                return createFallbackData(artworkId);
            }
            
            // Config null 체크
            if (config == null || config.getBaseUrl() == null) {
                log.error("Config or baseUrl is null!");
                return createFallbackData(artworkId);
            }
            
            ArtworkDetailDto result = null;
            try {
                result = restTemplate.getForObject(url, ArtworkDetailDto.class);
                log.debug("RestTemplate call completed");
            } catch (Exception e) {
                log.error("RestTemplate call failed for url: {}", url, e);
                return createFallbackData(artworkId);
            }
            
            if (result == null) {
                log.warn("Received null response from external service for artworkId: {}", artworkId);
                return createFallbackData(artworkId);
            }
            
            // 결과 검증
            if (result.getTitle() == null && result.getArtist() == null) {
                log.warn("Received empty data from external service for artworkId: {}", artworkId);
                return createFallbackData(artworkId);
            }
            
            log.debug("Successfully retrieved artwork detail for artworkId: {}", artworkId);
            return result;
            
        } catch (RestClientException e) {
            log.error("RestClient error while getting artwork detail for artworkId: {} - {}", artworkId, e.getMessage());
            return createFallbackData(artworkId);
        } catch (Exception e) {
            log.error("Unexpected error while getting artwork detail for artworkId: {}", artworkId, e);
            return createFallbackData(artworkId);
        }
    }

    private ArtworkDetailDto createFallbackData(Long artworkId) {
        log.info("Creating fallback data for artworkId: {}", artworkId);
        try {
            return ArtworkDetailDto.builder()
                    .artworkId(artworkId)
                    .title("Artwork " + artworkId)
                    .artist("Unknown Artist")
                    .imageUrl("https://via.placeholder.com/300x400?text=No+Image")
                    .build();
        } catch (Exception e) {
            log.error("Failed to create fallback data for artworkId: {}", artworkId, e);
            return null;
        }
    }
} 