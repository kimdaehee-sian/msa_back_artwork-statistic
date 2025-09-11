package com.example.artwork_statistics.controller;

import com.example.artwork_statistics.dto.TopArtworksResponseDto;
import com.example.artwork_statistics.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/artwork-stats")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Statistics", description = "작품 통계 관련 API")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class StatisticsController {
    
    private final StatisticsService statisticsService;
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("Test endpoint called");
        return ResponseEntity.ok("StatisticsController is working!");
    }
    
    @GetMapping("/top3")
    @Operation(summary = "상위 3개 작품 조회", description = "좋아요 수가 가장 많은 상위 3개 작품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회됨"),
            @ApiResponse(responseCode = "500", description = "서버 에러")
    })
    public ResponseEntity<TopArtworksResponseDto> getTop3Artworks() {
        log.info("Received request for top 3 artworks");
        
        try {
            TopArtworksResponseDto response = statisticsService.getTop3Artworks();
            log.info("Returning {} top artworks", response.getTopArtworks().size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred while getting top 3 artworks", e);
            throw e; // 다시 던져서 Spring의 글로벌 예외 처리기가 처리하도록 함
        }
    }
} 