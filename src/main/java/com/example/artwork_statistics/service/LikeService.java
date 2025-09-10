package com.example.artwork_statistics.service;

import com.example.artwork_statistics.dto.LikeRequestDto;
import com.example.artwork_statistics.entity.Like;
import com.example.artwork_statistics.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    
    private final LikeRepository likeRepository;
    
    @Transactional
    public void saveLike(LikeRequestDto request) {
        log.debug("Saving like for artwork: {}", request.getArtworkId());
        
        Like like = Like.builder()
                .artworkId(request.getArtworkId())
                .build();
        
        likeRepository.save(like);
        
        log.debug("Like saved for artwork: {}", request.getArtworkId());
    }
} 