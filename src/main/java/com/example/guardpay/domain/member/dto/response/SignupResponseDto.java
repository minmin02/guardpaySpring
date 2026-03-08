package com.example.guardpay.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponseDto {
    private Long memberId;
    private String email;
    private String nickname;
}