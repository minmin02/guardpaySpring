package com.example.guardpay.domain.shop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

    private Long id;
    private String brand;
    private String name;
    private String category;
    private Integer pricePoint;
    private String thumbnail;
}
