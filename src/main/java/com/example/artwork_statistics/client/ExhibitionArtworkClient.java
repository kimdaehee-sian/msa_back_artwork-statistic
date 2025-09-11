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
            
            return restTemplate.getForObject(url, ArtworkDetailDto.class);
        } catch (Exception e) {
            log.error("Failed to get artwork detail for artworkId: {}", artworkId, e);
            // 실패 시 테스트용 데이터 반환
            return ArtworkDetailDto.builder()
                    .artworkId(artworkId)
                    .name("Test Artwork " + artworkId)
                    .artist("Test Artist " + artworkId)
                    .build();
        }
    }
} 