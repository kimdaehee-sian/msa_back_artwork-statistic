package com.example.artwork_statistics.service;

import com.example.artwork_statistics.client.ExhibitionArtworkClient;
import com.example.artwork_statistics.dto.ArtworkDetailDto;
import com.example.artwork_statistics.dto.TopArtworkDto;
import com.example.artwork_statistics.dto.TopArtworksResponseDto;
import com.example.artwork_statistics.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {
    
    private final LikeRepository likeRepository;
    private final ExhibitionArtworkClient exhibitionArtworkClient;
    
    @Transactional(readOnly = true)
    public TopArtworksResponseDto getTop3Artworks() {
        log.debug("Getting top 3 artworks");
        
        // 1. Top3 artwork_id와 like_count 조회
        List<Object[]> top3Results = likeRepository.findTop3ArtworksByLikes();
        
        List<TopArtworkDto> topArtworks = new ArrayList<>();
        log.debug("top3Results size: {}", top3Results != null ? top3Results.size() : "null");
        
        if (top3Results != null && !top3Results.isEmpty()) {
            for (int i = 0; i < top3Results.size(); i++) {
                Object[] result = top3Results.get(i);
                log.debug("Database result {}: artworkId={}, likeCount={}", 
                        i, result != null && result.length > 0 ? result[0] : "null", 
                        result != null && result.length > 1 ? result[1] : "null");
            }
        }
        
        // 데이터가 없는 경우 처리
        if (top3Results == null || top3Results.isEmpty()) {
            log.warn("No artwork likes found in database, returning empty list");
            return TopArtworksResponseDto.builder()
                    .topArtworks(new ArrayList<>())
                    .build();
        }
        
        // 2. 각 artwork에 대해 외부 서비스에서 상세 정보 조회
        for (Object[] result : top3Results) {
            if (result == null || result.length < 2) {
                log.warn("Invalid result from database: {}", result);
                continue;
            }
            
            Long artworkId = (Long) result[0];
            Long likeCount = (Long) result[1];
            
            if (artworkId == null) {
                log.warn("artworkId is null, skipping");
                continue;
            }
            
            log.debug("Processing artwork: {} with {} likes", artworkId, likeCount);
            
            // 3. Exhibition Artwork Service에서 상세 정보 조회
            log.debug("=== DEBUGGING START ===");
            log.debug("exhibitionArtworkClient is null: {}", exhibitionArtworkClient == null);
            log.debug("artworkId: {}", artworkId);

            ArtworkDetailDto artworkDetail = null;
            try {
                artworkDetail = exhibitionArtworkClient.getArtworkDetail(artworkId);
                log.debug("Successfully got artworkDetail from client");
            } catch (Exception e) {
                log.error("Error getting artwork detail from client for artworkId: {}", artworkId, e);
            }

            log.debug("artworkDetail is null: {}", artworkDetail == null);
            if (artworkDetail != null) {
                log.debug("artworkDetail.getTitle(): {}", artworkDetail.getTitle());
                log.debug("artworkDetail.getArtist(): {}", artworkDetail.getArtist());
                log.debug("artworkDetail.getImageUrl(): {}", artworkDetail.getImageUrl());
            }
            log.debug("=== DEBUGGING END ===");

            // null 체크 및 fallback 처리 강화
            if (artworkDetail == null) {
                log.warn("artworkDetail is null for artworkId: {}, creating fallback data", artworkId);
                artworkDetail = createFallbackArtworkDetail(artworkId);
            }

            // 추가적인 null 체크
            if (artworkDetail == null) {
                log.error("Failed to create fallback data for artworkId: {}, skipping this artwork", artworkId);
                continue; // 이 artwork는 건너뛰고 다음으로
            }

            // 필드별 null 체크
            String title = artworkDetail.getTitle();
            String artist = artworkDetail.getArtist();
            String imageUrl = artworkDetail.getImageUrl();
            
            if (title == null) {
                log.warn("title is null for artworkId: {}, using fallback", artworkId);
                title = "Unknown Title " + artworkId;
            }
            
            if (artist == null) {
                log.warn("artist is null for artworkId: {}, using fallback", artworkId);
                artist = "Unknown Artist";
            }
            
            if (imageUrl == null) {
                log.warn("imageUrl is null for artworkId: {}, using fallback", artworkId);
                imageUrl = "https://via.placeholder.com/300x400?text=No+Image";
            }

            // 4. 응답 DTO 생성
            try {
                TopArtworkDto topArtwork = TopArtworkDto.builder()
                        .artworkId(artworkId)
                        .name(title)
                        .artist(artist)
                        .imageUrl(imageUrl)
                        .likeCount(likeCount)
                        .build();
                
                topArtworks.add(topArtwork);
                log.debug("Successfully created TopArtworkDto for artworkId: {}", artworkId);
            } catch (Exception e) {
                log.error("Failed to create TopArtworkDto for artworkId: {}", artworkId, e);
            }
        }
        
        log.debug("Found {} top artworks", topArtworks.size());
        
        return TopArtworksResponseDto.builder()
                .topArtworks(topArtworks)
                .build();
    }

    private ArtworkDetailDto createFallbackArtworkDetail(Long artworkId) {
        log.warn("Creating fallback artwork detail for artworkId: {}", artworkId);
        return ArtworkDetailDto.builder()
                .artworkId(artworkId)
                .title("Fallback Artwork " + artworkId)
                .artist("Fallback Artist " + artworkId)
                .imageUrl("https://via.placeholder.com/300x400?text=No+Image")
                .build();
    }
} 