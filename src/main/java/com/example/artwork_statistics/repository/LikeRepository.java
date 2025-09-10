package com.example.artwork_statistics.repository;

import com.example.artwork_statistics.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    @Query("SELECT l.artworkId, COUNT(l) as likeCount " +
           "FROM Like l " +
           "GROUP BY l.artworkId " +
           "ORDER BY COUNT(l) DESC " +
           "LIMIT 3")
    List<Object[]> findTop3ArtworksByLikes();
} 