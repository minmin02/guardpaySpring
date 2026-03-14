package com.example.guardpay.domain.map.controller;

import com.example.guardpay.domain.map.dto.req.BankSearchReq;
import com.example.guardpay.domain.map.dto.res.BankRes;
import com.example.guardpay.domain.map.service.BankService;
import com.example.guardpay.global.jwt.MemberUserDetails;
import com.example.guardpay.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @Operation(summary = "주변 은행 검색 API", description = "사용자 위치 기준 반경 내 은행을 검색합니다.")
    @GetMapping("/nearby")
    public ApiResponse<List<BankRes>> findNearbyBanks(
            @Valid @ModelAttribute BankSearchReq request,
            @AuthenticationPrincipal MemberUserDetails userDetails
    ) {
        List<BankRes> banks = bankService.findBanksNearby(request);
        return ApiResponse.ok(banks);
    }
}