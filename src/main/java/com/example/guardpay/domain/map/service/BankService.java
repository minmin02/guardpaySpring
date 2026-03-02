package com.example.guardpay.domain.map.service;

import com.example.guardpay.domain.map.dto.req.BankSearchReq;
import com.example.guardpay.domain.map.dto.res.BankRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate;

    /**
     * 카카오 로컬 API로 은행 검색 (실시간)
     */
    public List<BankRes> findBanksNearby(BankSearchReq request) {
        try {
            log.info("🏦 은행 검색 시작 - 이름: {}, 위치: ({}, {}), 반경: {}m",
                    request.getBankName(),
                    request.getLatitude(),
                    request.getLongitude(),
                    request.getRadius());

            // 카카오 로컬 API - 키워드 검색
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
                    .queryParam("query", request.getBankName())
                    .queryParam("x", request.getLongitude())
                    .queryParam("y", request.getLatitude())
                    .queryParam("radius", request.getRadius())
                    .queryParam("sort", "distance")  // 거리순 정렬
                    .queryParam("size", 15)  // 최대 15개
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> result = response.getBody();
                List<Map<String, Object>> documents = (List<Map<String, Object>>) result.get("documents");

                if (documents == null || documents.isEmpty()) {
                    log.warn("⚠️ 검색 결과 없음");
                    return List.of();
                }

                // 은행만 필터링 (카테고리 확인)
                List<BankRes> bankResList = documents.stream()
                        .filter(this::isBankCategory)
                        .map(doc -> convertToRes(doc, request.getLatitude(), request.getLongitude()))
                        .collect(Collectors.toList());

                log.info("✅ 은행 검색 완료: {}개 결과", bankResList.size());
                return bankResList;
            }

            return List.of();

        } catch (Exception e) {
            log.error("❌ 은행 검색 실패: {}", e.getMessage(), e);
            throw new RuntimeException("은행 검색 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 은행 카테고리인지 확인
     */
    private boolean isBankCategory(Map<String, Object> document) {
        String categoryName = (String) document.get("category_name");
        if (categoryName == null) return false;

        // 은행 카테고리: "금융,보험 > 은행"
        return categoryName.contains("은행") ||
                categoryName.contains("금융");
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