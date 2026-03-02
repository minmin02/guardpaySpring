package com.example.guardpay.domain.video.entity;
import com.example.guardpay.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "video_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class VideoCategory extends BaseEntity {

    //id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카테고리 이름 (예: "보이스피싱", "스미싱")
    @Column(nullable = false, length = 50)
    private String name;

    // 카테고리 설명
    @Column(length = 200)
    private String description;

    // 아이콘 이름 또는 URL
    @Column(length = 50)
    private String icon;

    // 표시 순서
    @Column(nullable = false)
    private Integer displayOrder;

    // 활성화 여부
    @Column(nullable = false)
    private Boolean isActive;

    // 카테고리 하나에 여러 영상
    // casecade =CascadeType.ALL
    // casecade =CascadeType.REMOVE
    // 같이 casecade 옵션
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreventionVideo> videos = new ArrayList<>();



    @PrePersist
    protected void onCreate() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        LocalDateTime updatedAt = LocalDateTime.now();
    }
}