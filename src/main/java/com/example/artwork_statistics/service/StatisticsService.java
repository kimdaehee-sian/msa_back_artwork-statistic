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
        System.out.println("top3Results: " + top3Results);
        
        // 2. 각 artwork에 대해 외부 서비스에서 상세 정보 조회
        for (Object[] result : top3Results) {
            Long artworkId = (Long) result[0];
            Long likeCount = (Long) result[1];
            
            log.debug("Processing artwork: {} with {} likes", artworkId, likeCount);
            
            // 3. Exhibition Artwork Service에서 상세 정보 조회
            log.debug("=== DEBUGGING START ===");
            log.debug("exhibitionArtworkClient is null: {}", exhibitionArtworkClient == null);
            log.debug("artworkId: {}", artworkId);

            ArtworkDetailDto artworkDetail = exhibitionArtworkClient.getArtworkDetail(artworkId);

            log.debug("artworkDetail is null: {}", artworkDetail == null);
            log.debug("artworkDetail: {}", artworkDetail);
            log.debug("=== DEBUGGING END ===");

            // null 체크 추가
            if (artworkDetail == null) {
                log.warn("artworkDetail is null for artworkId: {}, creating fallback data", artworkId);
                artworkDetail = ArtworkDetailDto.builder()
                        .artworkId(artworkId)
                        .title("Fallback Artwork " + artworkId)
                        .artist("Fallback Artist " + artworkId)
                        .imageUrl("https://via.placeholder.com/300x400?text=No+Image")
                        .build();
            }

            // 4. 응답 DTO 생성
            TopArtworkDto topArtwork = TopArtworkDto.builder()
                    .artworkId(artworkId)
                    .name(artworkDetail.getTitle())
                    .artist(artworkDetail.getArtist())
                    .imageUrl(artworkDetail.getImageUrl())
                    .likeCount(likeCount)
                    .build();
            
            topArtworks.add(topArtwork);
        }
        
        log.debug("Found {} top artworks", topArtworks.size());
        
        return TopArtworksResponseDto.builder()
                .topArtworks(topArtworks)
                .build();
    }
} 