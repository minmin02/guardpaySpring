package com.example.guardpay.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 플러터에서 받은 카카오 토큰
@Getter
@NoArgsConstructor
public class KakaoToken {
    private String accessToken;
}
