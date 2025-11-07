package com.example.guardpay.domain.video.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreventionVideoDto {
    private Long id;
    private String title;
    private String description;
    private String youtubeId;
    private String youtubeUrl;
    private String thumbnailUrl;
    private String embedUrl;
    private String duration;
    private Integer viewCount;
    private Integer displayOrder;
    private Long categoryId;
    private String categoryName;
}