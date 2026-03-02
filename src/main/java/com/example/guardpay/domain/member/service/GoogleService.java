package com.example.guardpay.domain.member.service;

import com.example.guardpay.domain.member.dto.response.AuthResponseDto;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.global.jwt.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${google.client-id}")
    private String googleClientId;

    /**
     * ✅ 구글 ID 토큰 검증 및 회원가입/로그인 처리
     */
    public AuthResponseDto loginOrSignup(String idTokenString) {
        try {
            // 1️⃣ 구글 ID 토큰 검증
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new IllegalArgumentException("Invalid Google ID Token");
            }

            // 2️⃣ 사용자 정보 추출
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String sub = payload.getSubject(); // Google 고유 사용자 ID

            // 3️⃣ 회원 조회 or 신규 등록
            boolean isNewUser = false;
            Member member = memberRepository.findByEmail(email).orElse(null);

            if (member == null) {
                // 신규 회원 생성
                member = memberRepository.save(
                        Member.createSocialMember(email, name, "google", sub)
                );
                isNewUser = true;
            }

            // 4️⃣ JWT 토큰 생성
            String accessToken = jwtTokenProvider.createAccessToken(member.getMemberId(), member.getRole());
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getMemberId());

            // 4-1️⃣ Refresh Token을 DB에 저장
            member.updateRefreshToken(refreshToken);
            memberRepository.save(member);

            // 5️⃣ 응답 반환
            return new AuthResponseDto(accessToken, refreshToken, isNewUser);

        } catch (Exception e) {
            throw new RuntimeException("Google login failed", e);
        }
    }
}
