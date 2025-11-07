package com.example.guardpay.domain.video.converter;

import com.example.guardpay.domain.video.dto.req.PreventionVideoDto;
import com.example.guardpay.domain.video.entity.PreventionVideo;
import org.springframework.stereotype.Component;

@Component
public class PreventionVideoConverter {

    /**
     * PreventionVideo Entity -> PreventionVideoDto 변환
     */
    public PreventionVideoDto toDto(PreventionVideo video) {
        if (video == null) {
            return null;
        }

        return PreventionVideoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .youtubeId(video.getYoutubeId())
                .youtubeUrl(video.getYoutubeUrl())
                .thumbnailUrl(video.getThumbnailUrl())
                .embedUrl(video.getEmbedUrl())
                .duration(video.getDuration())
                .viewCount(video.getViewCount())
                .displayOrder(video.getDisplayOrder())
                .categoryId(getCategoryId(video))
                .categoryName(getCategoryName(video))
                .build();
    }

    private Long getCategoryId(PreventionVideo video) {
        return video.getCategory() != null ? video.getCategory().getId() : null;
    }

    /**
     * 카테고리 이름 안전하게 가져오기
     */
    private String getCategoryName(PreventionVideo video) {
        return video.getCategory() != null ? video.getCategory().getName() : null;
    }
}
