package com.example.guardpay.domain.video.entity;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.shop.entity.ExchangeLog;
import com.example.guardpay.domain.shop.entity.Product;
import com.example.guardpay.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prevention_videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreventionVideo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title; // 영상 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 영상 설명

    @Column(nullable = false, length = 500)
    private String youtubeUrl; // 유튜브 URL (https://www.youtube.com/watch?v=VIDEO_ID)

    @Column(nullable = false, length = 20)
    private String youtubeId; // 유튜브 비디오 ID (VIDEO_ID만 추출)

    @Column(length = 500)
    private String thumbnailUrl; // 썸네일 URL (자동 생성 가능)

    @Column(length = 10)
    private String duration; // 영상 길이 (예: "5:30")

    @Column(nullable = false)
    private Integer viewCount; // 조회수

    @Column(nullable = false)
    private Integer displayOrder; // 카테고리 내 표시 순서

    @Column(nullable = false)
    private Boolean isActive; // 활성화 여부


    // 카테고리와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private VideoCategory category;


    @PrePersist
    protected void onCreate() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        if (viewCount == null) {
            viewCount = 0;
        }
        if (isActive == null) {
            isActive = true;
        }
        // 유튜브 URL에서 ID 자동 추출
        if (youtubeUrl != null && youtubeId == null) {
            this.youtubeId = extractYoutubeId(youtubeUrl);
        }
        // 썸네일 자동 생성
        if (thumbnailUrl == null && youtubeId != null) {
            this.thumbnailUrl = "https://img.youtube.com/vi/" + youtubeId + "/hqdefault.jpg";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        LocalDateTime updatedAt = LocalDateTime.now();
    }

    // 유튜브 URL에서 비디오 ID 추출
    private String extractYoutubeId(String url) {
        String videoId = null;

        // https://www.youtube.com/watch?v=VIDEO_ID 형식
        if (url.contains("watch?v=")) {
            int index = url.indexOf("watch?v=") + 8;
            videoId = url.substring(index);
            if (videoId.contains("&")) {
                videoId = videoId.substring(0, videoId.indexOf("&"));
            }
        }
        // https://youtu.be/VIDEO_ID 형식
        else if (url.contains("youtu.be/")) {
            int index = url.indexOf("youtu.be/") + 9;
            videoId = url.substring(index);
            if (videoId.contains("?")) {
                videoId = videoId.substring(0, videoId.indexOf("?"));
            }
        }

        return videoId;
    }


    //헬퍼 메소드

    // 임베드용 URL 생성
    public String getEmbedUrl() {
        if (youtubeId != null) {
            return "https://www.youtube.com/embed/" + youtubeId;
        }
        return null;
    }


}
