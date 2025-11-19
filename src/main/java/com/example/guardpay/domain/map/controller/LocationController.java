package com.example.guardpay.domain.map.controller;

import com.example.guardpay.domain.map.dto.res.LocationRes;
import com.example.guardpay.domain.map.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/search")
    public ResponseEntity<List<LocationRes>> search(@RequestParam String query) {
        log.info("🔍 검색: [{}]", query);
        List<LocationRes> results = locationService.searchKeyword(query);  // 👈 이 줄 확인
        log.info("✅ 결과: {}개", results.size());
        return ResponseEntity.ok(results);
    }
}