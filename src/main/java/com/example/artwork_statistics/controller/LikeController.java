package com.example.artwork_statistics.controller;

import com.example.artwork_statistics.dto.LikeRequestDto;
import com.example.artwork_statistics.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Slf4j
public class LikeController {
    
    private final LikeService likeService;
    
    @PostMapping
    public ResponseEntity<Void> saveLike(@RequestBody LikeRequestDto request) {
        log.info("Received like request for artwork: {}", request.getArtworkId());
        
        likeService.saveLike(request);
        
        return ResponseEntity.ok().build();
    }
} 