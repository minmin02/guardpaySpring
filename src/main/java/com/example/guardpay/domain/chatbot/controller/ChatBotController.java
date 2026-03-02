package com.example.guardpay.domain.chatbot.controller;

import com.example.guardpay.domain.chatbot.dto.req.ChatRequestDto;
import com.example.guardpay.domain.chatbot.service.FinancialEducationService; // ← 변경
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "챗봇", description = "챗봇 서비스")
public class ChatBotController {

    private final FinancialEducationService financialEducationService;

    @Operation(summary = "챗봇 입장 API", description = "프롬프트를 받고 작업시작")
    @PostMapping("/financial-advice")
    public ResponseEntity<?> getAdvice(
            @RequestBody ChatRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("🎯 [Chat Controller] === Request START ===");
        log.info("🎯 [Chat Controller] UserDetails: {}", userDetails);
        log.info("🎯 [Chat Controller] Principal name: {}",
                userDetails != null ? userDetails.getUsername() : "null");
        log.info("🎯 [Chat Controller] Request prompt: {}", requestDto.getPrompt());
        log.info("🎯 [Chat Controller] === Request END ===");

        try {
            // ✅ [변경] 프롬프트 파일 기반 금융 교육 서비스 호출
            String response = financialEducationService.generateEducationContent(
                    requestDto.getPrompt(),  // 사용자 질문
                    null,                     // analysisData (나중에 추가)
                    "20-30대",               // ageGroup (나중에 추가)
                    "일반"                    // interests (나중에 추가)
            );

            log.info("✅ [Chat Controller] Response generated successfully");

            Map<String, String> jsonResponse = Map.of("text", response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);

        } catch (Exception error) {
            log.error("❌ [Chat Controller] Error type: {}", error.getClass().getName());
            log.error("❌ [Chat Controller] Error message: {}", error.getMessage());
            log.error("❌ [Chat Controller] Stack trace: ", error);

            Map<String, String> errorResponse = Map.of("error", error.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }
}