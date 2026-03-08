package com.example.guardpay.domain.video.controller;

import com.example.guardpay.domain.video.dto.req.CategoryWithVideosDto;
import com.example.guardpay.domain.video.dto.req.PreventionVideoDto;
import com.example.guardpay.domain.video.dto.req.VideoCategoryDto;
import com.example.guardpay.domain.video.service.PreventionVideoService;
import com.example.guardpay.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
@Tag(name = "금융 취약 계층 교육 영상", description = "취약계층 전용 비디오 영상 페이지 ")
public class PreventionVideoController {

    // 서비스 계층
    private final PreventionVideoService videoService;

    /**
     * 전체 카테고리 목록 조회
     */
    @GetMapping("/categories")
    public ApiResponse<List<VideoCategoryDto>> getAllCategories(@AuthenticationPrincipal UserDetails userDetails) {
        List<VideoCategoryDto> categories = videoService.getAllCategories();
        return ApiResponse.ok(categories);
    }

    /**
     * 특정 카테고리의 영상 목록 조회
     */
    @GetMapping("/categories/{categoryId}")
    public ApiResponse<CategoryWithVideosDto> getCategoryWithVideos(
            @PathVariable Long categoryId,@AuthenticationPrincipal UserDetails userDetails) {
        CategoryWithVideosDto result = videoService.getCategoryWithVideos(categoryId);
        return ApiResponse.ok(result);
    }

    /**
     * 특정 영상 상세 조회
     */
    @GetMapping("/{videoId}")
    public ApiResponse<PreventionVideoDto> getVideoDetail(
            @PathVariable Long videoId,@AuthenticationPrincipal UserDetails userDetails) {
        PreventionVideoDto video = videoService.getVideoDetail(videoId);
        return ApiResponse.ok(video);
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
