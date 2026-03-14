package com.example.guardpay.domain.map.service;

import com.example.guardpay.domain.map.converter.MapConverter;
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
    private final KakaoLocalClient kakaoLocalClient;

    /**
     * 통합 검색: 키워드(은행) 검색 → 실패 시 주소 검색
     */
    public List<LocationRes> searchKeyword(String query) {
        // 1. 키워드 검색 (은행 카테고리 BK9 우선 적용)
        List<Map<String, Object>> keywordDocs = kakaoLocalClient.searchKeywords(query, "BK9");

        if (!keywordDocs.isEmpty()) {
            return keywordDocs.stream()
                    .map(MapConverter::toLocationResFromKeyword)
                    .toList();
        }

        // 2. 키워드 결과가 없으면 주소 검색 시도
        List<Map<String, Object>> addressDocs = kakaoLocalClient.searchAddress(query);

        return addressDocs.stream()
                .map(MapConverter::toLocationResFromAddress)
                .toList();
    }

}