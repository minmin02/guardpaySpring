package com.example.guardpay.domain.map.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoLocalClient {

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate;

    public List<Map<String, Object>> searchKeywords(String query, Double lat, Double lon, Double radius) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://dapi.kakao.com/v2/local/search/keyword.json")
                .queryParam("query", query)
                .queryParam("x", lon)
                .queryParam("y", lat)
                .queryParam("radius", radius)
                .queryParam("sort", "distance")
                .queryParam("size", 15)
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (List<Map<String, Object>>) response.getBody().get("documents");
            }
        } catch (Exception e) {
            log.error("Kakao API 호출 실패: {}", e.getMessage());
        }
        return Collections.emptyList();
    }


    /**
     * 주소 검색 (v2/local/search/address.json)
     */
    public List<Map<String, Object>> searchAddress(String address) {
        Map<String, String> params = Map.of(
                "query", address,
                "size", "10"
        );
        return search("/v2/local/search/address.json", params);
    }

    /**
     * 키워드 검색 (v2/local/search/keyword.json)
     */
    public List<Map<String, Object>> searchKeywords(String query, String categoryGroup) {
        Map<String, String> params = new HashMap<>();
        params.put("query", query);
        params.put("size", "15");
        if (categoryGroup != null) params.put("category_group_code", categoryGroup);

        return search("/v2/local/search/keyword.json", params);
    }

    private List<Map<String, Object>> search(String path, Map<String, String> params) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com" + path);
        params.forEach(builder::queryParam);

        URI uri = builder.build().encode(StandardCharsets.UTF_8).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            log.info("Kakao API Request URI: {}", uri);
            ResponseEntity<Map> response = restTemplate.exchange(
                    uri, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (List<Map<String, Object>>) response.getBody().get("documents");
            }
        } catch (Exception e) {
            log.error("Kakao API 호출 중 오류 발생: path={}, message={}", path, e.getMessage());
        }

        return Collections.emptyList();
    }


}