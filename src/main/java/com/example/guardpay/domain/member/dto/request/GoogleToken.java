package com.example.guardpay.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 플러터/안드로이드에서 받은 구글 ID 토큰
@Getter
@NoArgsConstructor
public class GoogleToken {
    private String idToken;
}
