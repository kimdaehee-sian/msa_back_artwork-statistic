package com.example.artwork_statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopArtworksResponseDto {
    
    private List<TopArtworkDto> topArtworks;
} 