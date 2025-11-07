package com.example.guardpay.domain.video.service;


import com.example.guardpay.domain.video.converter.PreventionVideoConverter;
import com.example.guardpay.domain.video.converter.VideoCategoryConverter;
import com.example.guardpay.domain.video.dto.req.CategoryWithVideosDto;
import com.example.guardpay.domain.video.dto.req.PreventionVideoDto;
import com.example.guardpay.domain.video.dto.req.VideoCategoryDto;
import com.example.guardpay.domain.video.entity.PreventionVideo;
import com.example.guardpay.domain.video.entity.VideoCategory;
import com.example.guardpay.domain.video.repository.PreventionVideoRepository;
import com.example.guardpay.domain.video.repository.VideoCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PreventionVideoService {

    private final VideoCategoryRepository categoryRepository;
    private final PreventionVideoRepository videoRepository;

    // Converter 주입
    private final VideoCategoryConverter categoryConverter;
    private final PreventionVideoConverter videoConverter;

    /**
     * 전체 카테고리 목록 조회
     */
    public List<VideoCategoryDto> getAllCategories() {
        // 카테고리 오름차순으로 영상 받아옴
        List<VideoCategory> categories = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();

        // stream은  리스트 요소들을 하나씩 처리해줌
        return categories.stream()
                .map(categoryConverter::toDto)  // DTO 변환
                .collect(Collectors.toList()); // 다시 수집
    }

    /**
     * 특정 카테고리의 영상 목록 조회
     */
    public CategoryWithVideosDto getCategoryWithVideos(Long categoryId) {
        VideoCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다: " + categoryId));

        List<PreventionVideo> videos = videoRepository
                .findByCategoryIdAndIsActiveTrueOrderByDisplayOrderAsc(categoryId);

        return CategoryWithVideosDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .categoryDescription(category.getDescription())
                .categoryIcon(category.getIcon())

                .videos(videos.stream()
                        .map(videoConverter::toDto)  // 컨버터 사용
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * 특정 영상 상세 조회
     */
    public PreventionVideoDto getVideoDetail(Long videoId) {
        PreventionVideo video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("영상을 찾을 수 없습니다: " + videoId));

        return videoConverter.toDto(video);  // 컨버터 사용
    }

    /**
     * 영상 조회수 증가
     */
    @Transactional
    public void incrementViewCount(Long videoId) {
        videoRepository.incrementViewCount(videoId);
    }

    /**
     * 인기 영상 목록 조회
     */
    public List<PreventionVideoDto> getPopularVideos() {
        List<PreventionVideo> videos = videoRepository.findTop10ByIsActiveTrueOrderByViewCountDesc();

        return videos.stream()
                .map(videoConverter::toDto)  // 컨버터 사용
                .collect(Collectors.toList());
    }

    /**
     * 전체 영상 목록 조회 
     */
    public List<PreventionVideoDto> getAllVideos() {
        List<PreventionVideo> videos = videoRepository.findByIsActiveTrueOrderByCreatedAtDesc();

        return videos.stream()
                .map(videoConverter::toDto)  // 컨버터 사용
                .collect(Collectors.toList());
    }





}