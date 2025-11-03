package com.example.guardpay.domain.video.service;

import com.example.guardpay.domain.video.entity.Video;
import com.example.guardpay.domain.video.repository.VideoRepository;
import com.example.guardpay.global.exception.VideoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    // 🎥 영상 목록 조회
    public Map<String, Object> getAllVideos() {
        List<Video> videos = videoRepository.findAll();

        List<Map<String, Object>> videoList = videos.stream()
                .map(v -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("videoId", v.getVideoId());
                    map.put("title", v.getTitle());
                    map.put("thumbnail", v.getThumbnailUrl());
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "영상 목록 조회 성공");
        response.put("data", Map.of("videos", videoList));

        return response;
    }

    // 🎬 영상 상세 조회
    public Map<String, Object> getVideoDetail(Integer videoId) {
        Video v = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("존재하지 않는 영상입니다."));

        Map<String, Object> videoData = new HashMap<>();
        videoData.put("videoId", v.getVideoId());
        videoData.put("title", v.getTitle());
        videoData.put("description", v.getDescription());
        videoData.put("url", v.getUrl());
        videoData.put("thumbnail", v.getThumbnailUrl());
        videoData.put("source", v.getSource());

        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "영상 상세 조회 성공");
        response.put("data", videoData);

        return response;
    }
}
