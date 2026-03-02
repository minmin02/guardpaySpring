package com.example.guardpay.global.auth;

import com.example.guardpay.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * 구글로부터 받은 사용자 정보를 Member 엔티티로 변환하기 위한 DTO
 */
@Getter
@Builder
public class OAuthAttributes {
    private Map<String, Object> attributes; // 원본 속성 맵
    private String nameAttributeKey;        // Principal의 식별자 키 (예: "sub")
    private String nickname;                // Member 엔티티의 'nickname'
    private String email;                   // Member 엔티티의 'email'
    private String provider;                // Member 엔티티의 'provider' (예: "google")
    private String providerId;              // Member 엔티티의 'providerId'

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        // 현재는 google만 지원하므로 google만 처리
        return ofGoogle(registrationId, userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String) attributes.get("name"))         // 구글의 'name'을 'nickname'으로 매핑
                .email((String) attributes.get("email"))
                .provider(registrationId)                           // "google"
                .providerId((String) attributes.get(userNameAttributeName)) // 구글의 고유 ID('sub')를 'providerId'로 매핑
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * Member 엔티티를 생성합니다.
     * @return Member 엔티티
     */
    public Member toEntity() {
        // 제공해주신 Member 엔티티의 createSocialMember 정적 메서드를 사용합니다.
        return Member.createSocialMember(email, nickname, provider, providerId);
    }
}