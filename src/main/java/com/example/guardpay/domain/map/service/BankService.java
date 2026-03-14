package com.example.guardpay.domain.map.service;

import com.example.guardpay.domain.map.converter.MapConverter;
import com.example.guardpay.domain.map.dto.req.BankSearchReq;
import com.example.guardpay.domain.map.dto.res.BankRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate;
    private final KakaoLocalClient kakaoLocalClient;

    public List<BankRes> findBanksNearby(BankSearchReq request) {

        // 1. API 호출
        List<Map<String, Object>> documents = kakaoLocalClient.searchKeywords(
                request.getBankName(),
                request.getLatitude(),
                request.getLongitude(),
                request.getRadius()
        );

        return documents.stream()
                .filter(this::isBankCategory)
                .map(MapConverter::toBankRes)
                .toList();
    }

    /**
     * 은행 카테고리인지 확인
     */
    private boolean isBankCategory(Map<String, Object> doc) {
        String category = (String) doc.get("category_name");
        return category != null && (category.contains("은행") || category.contains("금융"));
    }


    /**
     * 카카오 API 응답을 BankRes로 변환
     */
    private BankRes convertToRes(Map<String, Object> document, Double userLat, Double userLon) {
        String placeName = (String) document.get("place_name");
        String address = (String) document.get("address_name");
        String roadAddress = (String) document.get("road_address_name");
        Double lat = Double.parseDouble((String) document.get("y"));
        Double lon = Double.parseDouble((String) document.get("x"));
        String phone = (String) document.get("phone");
        String distance = (String) document.get("distance");

        BankRes res = BankRes.builder()
                .id(Long.parseLong((String) document.get("id")))
                .name(extractBankName(placeName))
                .branchName(extractBranchName(placeName))
                .fullName(placeName)
                .address(address)
                .roadAddress(roadAddress)
                .lat(lat)
                .lon(lon)
                .phoneNumber(phone)
                .build();

        // 거리 설정
        if (distance != null && !distance.isEmpty()) {
            try {
                double distanceMeters = Double.parseDouble(distance);
                res.setDistance(distanceMeters);
            } catch (NumberFormatException e) {
                log.warn("거리 파싱 실패: {}", distance);
            }
        }

        return res;
    }

    /**
     * 장소명에서 은행명 추출
     * 예: "신한은행 강남역지점" -> "신한은행"
     */
    private String extractBankName(String placeName) {
        if (placeName == null) return "";

        String[] bankNames = {
                "신한은행", "국민은행", "우리은행", "하나은행",
                "NH농협은행", "기업은행", "SC제일은행", "씨티은행",
                "부산은행", "대구은행", "경남은행", "광주은행",
                "전북은행", "제주은행", "수협은행", "새마을금고",
                "카카오뱅크", "케이뱅크", "토스뱅크"
        };

        for (String bankName : bankNames) {
            if (placeName.contains(bankName)) {
                return bankName;
            }
        }

        return placeName.split(" ")[0];  // 첫 단어 반환
    }

    /**
     * 장소명에서 지점명 추출
     * 예: "신한은행 강남역지점" -> "강남역지점"
     */
    private String extractBranchName(String placeName) {
        if (placeName == null) return "";

        String bankName = extractBankName(placeName);
        return placeName.replace(bankName, "").trim();
    }
}