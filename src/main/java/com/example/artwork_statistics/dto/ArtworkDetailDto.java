package com.example.artwork_statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtworkDetailDto {
    
    @JsonProperty("id")
    private Long artworkId;
    private String title;
    private String artist;
    private String era;
    private String description;
    private String imageUrl;
    private Long exhibitionId;
    private String createdAt;
} 