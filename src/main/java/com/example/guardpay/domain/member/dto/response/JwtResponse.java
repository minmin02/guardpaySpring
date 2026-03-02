package com.example.guardpay.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String tokenType;     // "Bearer"
    private String accessToken;
    private String refreshToken;
    private Long   expiresIn;         // (초) access 만료
    private Long   refreshExpiresIn;  // (선택) refresh 만료


}
