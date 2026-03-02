package com.example.guardpay.domain.video.dto.req;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryWithVideosDto {
    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
    private String categoryIcon;
    private List<PreventionVideoDto> videos;
}