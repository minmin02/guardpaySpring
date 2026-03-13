package com.example.guardpay.domain.quiz.enums;

import com.example.guardpay.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuizErrorCode implements ResponseCode {
    // 카테고리 관련
    CATEGORY_NOT_FOUND("QUIZ_001", "존재하지 않는 카테고리입니다"),

    // 퀴즈 관련
    QUIZ_NOT_FOUND("QUIZ_002", "존재하지 않는 퀴즈입니다"),
    OPTION_NOT_FOUND("QUIZ_003", "존재하지 않는 선택지입니다"),
    QUIZ_ALREADY_SOLVED("QUIZ_004", "이미 정답을 맞힌 퀴즈입니다"),

    // 멤버 및 진행도 관련
    MEMBER_NOT_FOUND("QUIZ_005", "존재하지 않는 회원입니다"),
    PROGRESS_NOT_FOUND("QUIZ_006", "진행도 기록을 찾을 수 없습니다"),
    MEMBER_LEVEL_NOT_FOUND("QUIZ_007", "회원 레벨 정보를 찾을 수 없습니다");

    private final String statusCode;
    private final String message;

}