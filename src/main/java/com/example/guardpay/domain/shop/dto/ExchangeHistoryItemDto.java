package com.example.guardpay.domain.shop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeHistoryItemDto {

    private Long exchangeId;
    private String productName;
    private String brandName;
    private Integer pointsUsed;
    private String status;
    private String exchangedAt;
    private String couponCode;
    private String thumbnail;
    private String validUntil;
}
