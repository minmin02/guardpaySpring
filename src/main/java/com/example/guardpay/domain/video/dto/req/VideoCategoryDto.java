package com.example.guardpay.domain.video.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoCategoryDto {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private Integer displayOrder;
    private Integer videoCount;
}