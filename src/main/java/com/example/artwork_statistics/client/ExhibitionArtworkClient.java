package com.example.artwork_statistics.client;

import com.example.artwork_statistics.config.ExternalServiceConfig;
import com.example.artwork_statistics.dto.ArtworkDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExhibitionArtworkClient {
    
    private final RestTemplate restTemplate;
    private final ExternalServiceConfig config;
    
    public ArtworkDetailDto getArtworkDetail(Long artworkId) {
        try {
            String url = config.getBaseUrl() + "/api/artworks/" + artworkId;
            log.debug("Calling Exhibition Artwork Service: {}", url);
            
            ArtworkDetailDto result = restTemplate.getForObject(url, ArtworkDetailDto.class);
            
            // null 체크 추가
            if (result == null) {
                log.warn("Received null response from external service for artworkId: {}", artworkId);
                return createTestData(artworkId);
            }
            
            return result;
        } catch (Exception e) {
            log.error("Failed to get artwork detail for artworkId: {}", artworkId, e);
            return createTestData(artworkId);
        }
    }

    private ArtworkDetailDto createTestData(Long artworkId) {
        return ArtworkDetailDto.builder()
                .artworkId(artworkId)
                .name("Test Artwork " + artworkId)
                .artist("Test Artist " + artworkId)
                .build();
    }
} 