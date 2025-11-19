package com.example.guardpay.domain.map.controller;

import com.example.guardpay.domain.map.dto.req.BankSearchReq;
import com.example.guardpay.domain.map.dto.res.BankRes;
import com.example.guardpay.domain.map.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    /**
     * 주변 은행 검색 API
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<BankRes>> findNearbyBanks(
            @RequestParam String bankName,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5000") Double radius,
            Authentication authentication
    ) {
        try {
            log.info("GET /api/banks/nearby - bank: {}, location: ({}, {}), radius: {}m",
                    bankName, latitude, longitude, radius);

            if (authentication != null) {
                log.info("User: {}", authentication.getName());
            }

            // Request DTO 생성
            BankSearchReq request = BankSearchReq.builder()
                    .bankName(bankName)
                    .latitude(latitude)
                    .longitude(longitude)
                    .radius(radius)
                    .build();

            List<BankRes> banks = bankService.findBanksNearby(request);
            return ResponseEntity.ok(banks);

        } catch (Exception e) {
            log.error("은행 검색 실패: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}