package com.example.guardpay.domain.video.controller;

import com.example.guardpay.domain.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/videos")
public class VideoApiController {

    private final VideoService videoService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVideos() {
        Map<String, Object> response = videoService.getAllVideos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<Map<String, Object>> getVideoDetail(@PathVariable Integer videoId) {
        Map<String, Object> response = videoService.getVideoDetail(videoId);
        return ResponseEntity.ok(response);
    }
}
