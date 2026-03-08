package com.example.guardpay.domain.beneficiaries.controller;

import com.example.guardpay.domain.beneficiaries.service.BeneficiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/beneficiaries")
@Tag(name = "가상 계좌", description = "가상 계좌 조회 및 송금 훈련 API")
public class BeneficiaryApiController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping("/random")
    @Operation(summary = "랜덤 가상계좌 조회", description = "하루 1회 갱신되는 랜덤 가상계좌 4개 조회")
    public ResponseEntity<Map<String, Object>> getRandomBeneficiaries() {
        return ResponseEntity.ok(beneficiaryService.getRandomBeneficiaries());
    }

    @GetMapping("/{id}")
    @Operation(summary = "가상계좌 상세 조회", description = "선택한 가상계좌의 상세 정보 조회")
    public ResponseEntity<Map<String, Object>> getBeneficiaryDetail(@PathVariable Long id) {
        return ResponseEntity.ok(beneficiaryService.getBeneficiaryDetail(id));
    }

    @PostMapping("/{beneficiaryId}/transfer")
    @Operation(summary = "모의 송금", description = "포인트 송금 후 리워드 지급")
    public ResponseEntity<Map<String, Object>> transfer(
            @PathVariable Long beneficiaryId,
            @RequestBody Map<String, Integer> body,
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        int amount = body.get("amount");
        return ResponseEntity.ok(beneficiaryService.transfer(memberId, beneficiaryId, amount));
    }

}
