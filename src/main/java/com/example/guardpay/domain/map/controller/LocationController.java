package com.example.guardpay.domain.map.controller;

import com.example.guardpay.domain.map.dto.res.LocationRes;
import com.example.guardpay.domain.map.service.LocationService;
import com.example.guardpay.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "장소 검색 API", description = "키워드 또는 주소를 통해 위치 정보를 검색합니다.")
    @GetMapping("/search")
    public ApiResponse<List<LocationRes>> search(@RequestParam String query) {
        List<LocationRes> results = locationService.searchKeyword(query);
        return ApiResponse.ok(results);
    }
}