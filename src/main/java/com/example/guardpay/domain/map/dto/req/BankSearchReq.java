package com.example.guardpay.domain.map.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankSearchReq {

    @NotBlank(message = "은행명을 입력해주세요")
    private String bankName;

    @NotNull(message = "위도를 입력해주세요")
    @DecimalMin(value = "-90.0", message = "위도는 -90 ~ 90 사이여야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 -90 ~ 90 사이여야 합니다")
    private Double latitude;

    @NotNull(message = "경도를 입력해주세요")
    @DecimalMin(value = "-180.0", message = "경도는 -180 ~ 180 사이여야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 -180 ~ 180 사이여야 합니다")
    private Double longitude;

    @Min(value = 100, message = "최소 반경은 100m입니다")
    @Max(value = 10000, message = "최대 반경은 10km입니다")
    private Double radius = 5000.0; // 기본값 5km
}