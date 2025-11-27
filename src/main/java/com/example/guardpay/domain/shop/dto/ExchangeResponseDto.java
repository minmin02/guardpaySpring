package com.example.guardpay.domain.shop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeResponseDto {

    private Long productId;
    private String brand;
    private String name;
    private String thumbnail;

    private String couponCode;
    private String validUntil;
}
