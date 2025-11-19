package com.example.guardpay.domain.map.service;

import com.example.guardpay.domain.map.dto.res.LocationRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate;

    /**
     * 통합 검색: 키워드(은행) 검색 → 실패 시 주소 검색
     */
    public List<LocationRes> searchKeyword(String keyword) {
        // 1. 키워드 검색 시도 (은행 필터 적용)
        List<LocationRes> results = searchByKeyword(keyword);

        if (!results.isEmpty()) {
            log.info("✅ 키워드(은행) 검색 성공: {}개", results.size());
            return results;
        }

        // 2. 키워드 검색 실패 시 주소 검색
        log.info("🔄 키워드 검색 실패, 주소 검색 시도...");
        results = searchByAddress(keyword);

        if (!results.isEmpty()) {
            log.info("✅ 주소 검색 성공: {}개", results.size());
            return results;
        }

        log.warn("⚠️ 모든 검색 실패");
        return Collections.emptyList();
    }

    /**
     * 카카오 키워드 검색 (은행 카테고리 BK9 적용)
     */
    private List<LocationRes> searchByKeyword(String keyword) {
        try {
            log.info("🔍 키워드 검색 요청: [{}]", keyword);

            // URI 객체로 생성 (이중 인코딩 방지)
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
                    .queryParam("query", keyword)
                    .queryParam("category_group_code", "BK9") // 🏦 은행 코드 필터링
                    .queryParam("size", 15)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

            ResponseEntity<Map> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> documents =
                        (List<Map<String, Object>>) response.getBody().get("documents");

                if (documents != null && !documents.isEmpty()) {
                    return documents.stream()
                            .map(this::convertToLocationRes)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            }

            return Collections.emptyList();

        } catch (Exception e) {
            log.error("❌ 키워드 검색 에러: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 카카오 주소 검색
     */
    private List<LocationRes> searchByAddress(String address) {
        try {
            log.info("🔍 주소 검색 요청: [{}]", address);

            // URI 객체로 생성 (이중 인코딩 방지)
            URI uri = UriComponentsBuilder
                    .fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
                    .queryParam("query", address)
                    .queryParam("size", 10)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);

            ResponseEntity<Map> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> documents =
                        (List<Map<String, Object>>) response.getBody().get("documents");

                if (documents != null && !documents.isEmpty()) {
                    return documents.stream()
                            .map(this::convertAddressToLocationRes)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                }
            }

            return Collections.emptyList();

        } catch (Exception e) {
            log.error("❌ 주소 검색 에러: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 키워드 검색 결과 변환
     */
    private LocationRes convertToLocationRes(Map<String, Object> doc) {
        try {
            String placeName = (String) doc.get("place_name");
            String address = (String) doc.get("address_name");
            String roadAddress = (String) doc.get("road_address_name");

            Double lat = toDouble(doc.get("y"));
            Double lon = toDouble(doc.get("x"));

            if (lat == null || lon == null) return null;

            return LocationRes.builder()
                    .placeName(placeName)
                    .address(address != null ? address : "")
                    .roadAddress(roadAddress)
                    .latitude(lat)
                    .longitude(lon)
                    .build();
        } catch (Exception e) {
            log.error("❌ 변환 실패", e);
            return null;
        }
    }

    /**
     * 주소 검색 결과 변환
     */
    private LocationRes convertAddressToLocationRes(Map<String, Object> doc) {
        try {
            // 주소 검색은 address, road_address 객체로 옴
            Map<String, Object> addressObj = (Map<String, Object>) doc.get("address");
            Map<String, Object> roadAddressObj = (Map<String, Object>) doc.get("road_address");

            String address = null;
            String roadAddress = null;
            Double lat = null;
            Double lon = null;

            // 지번 주소
            if (addressObj != null) {
                address = (String) addressObj.get("address_name");
                lat = toDouble(addressObj.get("y"));
                lon = toDouble(addressObj.get("x"));
            }

            // 도로명 주소
            if (roadAddressObj != null) {
                roadAddress = (String) roadAddressObj.get("address_name");
                if (lat == null) {
                    lat = toDouble(roadAddressObj.get("y"));
                    lon = toDouble(roadAddressObj.get("x"));
                }
            }

            if (lat == null || lon == null) return null;

            return LocationRes.builder()
                    .placeName(roadAddress != null ? roadAddress : address)
                    .address(address != null ? address : "")
                    .roadAddress(roadAddress)
                    .latitude(lat)
                    .longitude(lon)
                    .build();
        } catch (Exception e) {
            log.error("❌ 주소 변환 실패", e);
            return null;
        }
    }

    private Double toDouble(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof String) return Double.parseDouble((String) value);
            if (value instanceof Number) return ((Number) value).doubleValue();
        } catch (Exception e) {
            log.warn("⚠️ Double 변환 실패: {}", value);
        }
        return null;
    }
}