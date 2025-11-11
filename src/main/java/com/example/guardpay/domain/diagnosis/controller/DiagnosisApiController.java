package com.example.guardpay.domain.diagnosis.controller;

import com.example.guardpay.domain.diagnosis.service.DiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diagnoses")
@Tag(name = "역량진단", description = "역량진단 제출 및 이력 조회")
public class DiagnosisApiController {

    private final DiagnosisService diagnosisService;

    @PostMapping("/submit")
    @Operation(summary = "제출", description = "역량진단 제출")
    public ResponseEntity<Map<String, Object>> submitDiagnosis(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> request) {

        String jwtToken = token.replace("Bearer ", "");
        List<Map<String, Object>> answers = (List<Map<String, Object>>) request.get("answers");
        return ResponseEntity.ok(diagnosisService.submitDiagnosis(jwtToken, answers));
    }

    @GetMapping("/history/{historyId}")
    @Operation(summary = "이력 조회", description = "역량진단 제출 이력 조회")
    public ResponseEntity<Map<String, Object>> getHistory(@PathVariable Long historyId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisHistory(historyId));
    }
}
