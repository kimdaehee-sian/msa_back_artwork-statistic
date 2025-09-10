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
        
        // 2. 각 artwork에 대해 외부 서비스에서 상세 정보 조회
        for (Object[] result : top3Results) {
            Long artworkId = (Long) result[0];
            Long likeCount = (Long) result[1];
            
            log.debug("Processing artwork: {} with {} likes", artworkId, likeCount);
            
            // 3. Exhibition Artwork Service에서 상세 정보 조회
            ArtworkDetailDto artworkDetail = exhibitionArtworkClient.getArtworkDetail(artworkId);
            
            // 4. 응답 DTO 생성
            TopArtworkDto topArtwork = TopArtworkDto.builder()
                    .artworkId(artworkId)
                    .name(artworkDetail.getName())
                    .artist(artworkDetail.getArtist())
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