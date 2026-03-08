package com.example.guardpay.domain.shop.enums;

import com.example.guardpay.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShopErrorCode implements ResponseCode {


    // 상품 관련
    PRODUCT_NOT_FOUND("SHOP_001", "상품을 찾을 수 없습니다"),
    PRODUCT_OUT_OF_STOCK("SHOP_002", "상품의 재고가 부족합니다"),

    // 포인트 관련
    INSUFFICIENT_POINTS("SHOP_003", "포인트가 부족합니다"),
    INVALID_POINT_AMOUNT("SHOP_004", "유효하지 않은 포인트 금액입니다"),

    // 교환 관련
    EXCHANGE_LOG_NOT_FOUND("SHOP_005", "교환 내역을 찾을 수 없습니다"),
    EXCHANGE_ALREADY_USED("SHOP_006", "이미 사용된 쿠폰입니다"),
    EXCHANGE_EXPIRED("SHOP_007", "유효기간이 만료된 쿠폰입니다"),
    INVALID_EXCHANGE_STATUS("SHOP_008", "유효하지 않은 교환 상태입니다"),

    // 쿠폰 관련
    INVALID_COUPON_CODE("SHOP_009", "유효하지 않은 쿠폰 코드입니다"),
    COUPON_GENERATION_FAILED("SHOP_010", "쿠폰 생성에 실패했습니다");

    private final String statusCode;
    private final String message;
}
