package com.example.guardpay.domain.video.repository;

import com.example.guardpay.domain.video.entity.VideoCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoCategoryRepository extends JpaRepository<VideoCategory, Long> {

    // 활성화된 카테고리만 조회 (표시 순서대로)
    List<VideoCategory> findByIsActiveTrueOrderByDisplayOrderAsc();

    // 카테고리명으로 조회
    Optional<VideoCategory> findByName(String name);

    // 카테고리와 영상을 함께 조회 (N+1 문제 해결)
    @Query("SELECT c FROM VideoCategory c LEFT JOIN FETCH c.videos WHERE c.id = :id AND c.isActive = true")
    Optional<VideoCategory> findByIdWithVideos(Long id);

}