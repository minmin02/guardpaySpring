package com.example.guardpay.domain.chatbot.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "챗봇 응답 DTO")
public record ChatResponseDto(
        @Schema(description = "AI가 생성한 답변 텍스트")
        String text,

        @Schema(description = "응답 생성 시간")
        LocalDateTime createdAt
) {
    public ChatResponseDto(String text) {
        this(text, LocalDateTime.now());
    }
}