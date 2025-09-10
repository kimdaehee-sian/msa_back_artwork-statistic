package com.example.artwork_statistics.controller;

import com.example.artwork_statistics.dto.LikeRequestDto;
import com.example.artwork_statistics.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Likes", description = "작품 좋아요 관련 API")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class LikeController {
    
    private final LikeService likeService;
    
    @PostMapping
    @Operation(summary = "작품 좋아요 저장", description = "사용자가 작품에 대한 좋아요를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요가 성공적으로 저장됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    public ResponseEntity<Void> saveLike(@RequestBody LikeRequestDto request) {
        log.info("Received like request for artwork: {}", request.getArtworkId());
        
        likeService.saveLike(request);
        
        return ResponseEntity.ok().build();
    }
} 