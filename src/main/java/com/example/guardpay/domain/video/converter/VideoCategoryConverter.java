package com.example.guardpay.domain.video.converter;

import com.example.guardpay.domain.video.dto.req.VideoCategoryDto;
import com.example.guardpay.domain.video.entity.PreventionVideo;
import com.example.guardpay.domain.video.entity.VideoCategory;
import org.springframework.stereotype.Component;

@Component
public class VideoCategoryConverter {
    /**
     * VideoCategory Entity -> VideoCategoryDto 변환
     */
    public VideoCategoryDto toDto(VideoCategory category) {
        if (category == null) {
            return null;
        }

        return VideoCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .icon(category.getIcon())
                .displayOrder(category.getDisplayOrder())
                .videoCount(countActiveVideos(category))
                .build();
    }


    private int countActiveVideos(VideoCategory category) {
        if (category.getVideos() == null) {
            return 0;
        }

        return (int) category.getVideos().stream()
                .filter(PreventionVideo::getIsActive)
                .count();
    }

}
