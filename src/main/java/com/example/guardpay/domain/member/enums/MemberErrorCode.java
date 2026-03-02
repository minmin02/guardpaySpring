package com.example.guardpay.domain.member.enums;


import com.example.guardpay.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ResponseCode {

    EMAIL_ALREADY_EXISTS("MEMBER_001", "이미 사용 중인 이메일입니다"),
    EMAIL_NOT_FOUND("MEMBER_002", "존재하지 않는 이메일입니다"),
    INVALID_PASSWORD("MEMBER_003", "비밀번호가 일치하지 않습니다"),
    MEMBER_NOT_FOUND("MEMBER_004", "회원을 찾을 수 없습니다");

    private final String statusCode;
    private final String message;
}