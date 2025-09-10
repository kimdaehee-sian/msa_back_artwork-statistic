package com.example.artwork_statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtworkDetailDto {
    
    private Long artworkId;
    private String name;
    private String artist;
} 