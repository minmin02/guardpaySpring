package com.example.guardpay.domain.chatbot.controller;

import com.example.guardpay.domain.chatbot.dto.req.ChatRequestDto;
import com.example.guardpay.domain.chatbot.service.GeminiChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// ❗️ Mono import는 제거하고 Map을 import합니다.
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatBotController {

    private final GeminiChatService geminiChatService;

    @PostMapping("/financial-advice")
    // ✅ [수정 1] 반환 타입을 Mono<>가 아닌 ResponseEntity<?>로 변경
    public ResponseEntity<?> getAdvice(
            @RequestBody ChatRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("🎯 [Chat Controller] === Request START ===");
        log.info("🎯 [Chat Controller] UserDetails: {}", userDetails);
        log.info("🎯 [Chat Controller] Principal name: {}",
                userDetails != null ? userDetails.getUsername() : "null");
        log.info("🎯 [Chat Controller] Authorities: {}",
                userDetails != null ? userDetails.getAuthorities() : "null");
        log.info("🎯 [Chat Controller] Request prompt: {}", requestDto.getPrompt());
        log.info("🎯 [Chat Controller] === Request END ===");

        // ✅ [수정 2] 서비스에 전달할 SecurityContext를 가져옵니다.
        SecurityContext context = SecurityContextHolder.getContext();

        try {
            // ✅ [수정 3] .block()을 호출하여 Mono가 완료될 때까지 '기다립니다'.
            String response = geminiChatService.getFinancialAdvice(requestDto.getPrompt(), context)
                    .block(); // 👈 이 스레드(인증된 스레드)가 여기서 대기합니다.

            log.info("✅ [Chat Controller] Response generated successfully");

            // ✅ [수정 4] 동기식으로 'JSON 객체(Map)'를 반환합니다.
            // Spring이 이 Map을 {"text": "AI 응답..."} JSON 문자열로 자동 변환합니다.
            Map<String, String> jsonResponse = Map.of("text", response);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON) // 이제 이 헤더는 진실입니다.
                    .body(jsonResponse); // Map 객체를 반환

        } catch (Exception error) {
            // ✅ [수정 5] .onErrorResume 대신 try-catch로 에러를 처리합니다.
            log.error("❌ [Chat Controller] Error type: {}", error.getClass().getName());
            log.error("❌ [Chat Controller] Error message: {}", error.getMessage());
            log.error("❌ [Chat Controller] Stack trace: ", error);
            log.error("❌ [Chat Controller] Returning error response");

            // ✅ [수정 6] 에러 응답도 Map 객체로 통일
            Map<String, String> errorResponse = Map.of("error", error.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }
}