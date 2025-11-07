package com.example.guardpay.domain.video.controller;

import com.example.guardpay.domain.video.dto.req.CategoryWithVideosDto;
import com.example.guardpay.domain.video.dto.req.PreventionVideoDto;
import com.example.guardpay.domain.video.dto.req.VideoCategoryDto;
import com.example.guardpay.domain.video.service.PreventionVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class PreventionVideoController {

    // 서비스 계층
    private final PreventionVideoService videoService;

    /**
     * 전체 카테고리 목록 조회
     * GET /api/videos/categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<VideoCategoryDto>> getAllCategories() {
        List<VideoCategoryDto> categories = videoService.getAllCategories();

        // 성공 요청
        return ResponseEntity.ok(categories);
    }

    /**
     * 특정 카테고리의 영상 목록 조회
     * GET /api/videos/categories/{categoryId}
     */
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryWithVideosDto> getCategoryWithVideos(
            @PathVariable Long categoryId) {
        CategoryWithVideosDto result = videoService.getCategoryWithVideos(categoryId);
        return ResponseEntity.ok(result);
    }

    /**
     * 특정 영상 상세 조회
     * GET /api/videos/{videoId}
     */
    @GetMapping("/{videoId}")
    public ResponseEntity<PreventionVideoDto> getVideoDetail(
            @PathVariable Long videoId) {
        PreventionVideoDto video = videoService.getVideoDetail(videoId);
        return ResponseEntity.ok(video);
    }

    /**
     * 영상 조회수 증가
     * POST /api/videos/{videoId}/view
     */
    @PostMapping("/{videoId}/view")
    public ResponseEntity<Void> incrementViewCount(
            @PathVariable Long videoId) {
        videoService.incrementViewCount(videoId);
        return ResponseEntity.ok().build();
    }

    /**
     * 인기 영상 목록 조회 (TOP 10)
     * GET /api/videos/popular
     */
    @GetMapping("/popular")
    public ResponseEntity<List<PreventionVideoDto>> getPopularVideos() {
        List<PreventionVideoDto> videos = videoService.getPopularVideos();
        return ResponseEntity.ok(videos);
    }

    /**
     * 전체 영상 목록 조회 (최신순)
     * GET /api/videos/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<PreventionVideoDto>> getAllVideos() {
        List<PreventionVideoDto> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }
}
