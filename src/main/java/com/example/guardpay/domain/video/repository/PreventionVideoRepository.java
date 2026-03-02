package com.example.guardpay.domain.video.repository;
import com.example.guardpay.domain.video.entity.PreventionVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreventionVideoRepository extends JpaRepository<PreventionVideo, Long> {

    // 특정 카테고리의 활성화된 영상 조회 (표시 순서대로)
    List<PreventionVideo> findByCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(Long categoryId);

    // 활성화된 영상만 조회
    List<PreventionVideo> findByIsActiveTrueOrderByCreatedAtDesc();

    // 조회수 증가
    @Modifying
    @Query("UPDATE PreventionVideo v SET v.viewCount = v.viewCount + 1 WHERE v.id = :id")
    void incrementViewCount(@Param("id") Long id);

    // 유튜브 ID로 조회
    Optional<PreventionVideo> findByYoutubeId(String youtubeId);

    // 인기 영상 조회 (조회수 높은 순)
    List<PreventionVideo> findTop10ByIsActiveTrueOrderByViewCountDesc();
}