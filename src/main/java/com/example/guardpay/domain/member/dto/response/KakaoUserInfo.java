package com.example.guardpay.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;


//카카오 응답을 담을 KakaoUserInfo DTO

@Getter
@JsonIgnoreProperties(ignoreUnknown = true) // 정의되지 않은 필드는 무시
public class KakaoUserInfo {

    private Long id; // 카카오 사용자의 고유 ID

    private KakaoAccount kakao_account; // 카카오 계정 정보

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        private Profile profile;
        private String email;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
        private String nickname;
    }
}