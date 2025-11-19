package com.example.guardpay.domain.map.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationRes {

    private String address;         // 지번 주소
    private String roadAddress;     // 도로명 주소
    private Double latitude;        // 위도 (y)
    private Double longitude;       // 경도 (x)
    private String placeName;       // 장소명 (키워드 검색 시)
    private String addressType;     // 주소 타입 (REGION, ROAD, REGION_ADDR, ROAD_ADDR)
}