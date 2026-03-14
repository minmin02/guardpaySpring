package com.example.guardpay.domain.chatbot.enums;


import com.example.guardpay.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements ResponseCode {
    CHAT_API_ERROR("CHAT_001", "AI 서비스 응답 중 오류가 발생했습니다."),
    PROMPT_LOAD_FAILED("CHAT_002", "프롬프트 템플릿을 불러오는 데 실패했습니다."),
    SAFETY_BLOCK("CHAT_003", "안전 정책에 의해 답변이 제한되었습니다."),
    EMPTY_RESPONSE("CHAT_004", "AI로부터 빈 응답을 받았습니다.");

    private final String statusCode;
    private final String message;
}