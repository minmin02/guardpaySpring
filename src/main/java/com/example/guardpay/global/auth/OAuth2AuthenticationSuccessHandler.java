package com.example.guardpay.global.auth;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 1) OAuth2 사용자 정보
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            // IdP별로 email 속성 키가 다를 수 있으니 필요하면 별도 매핑 처리
            throw new IllegalArgumentException("OAuth2 provider did not return an email attribute.");
        }

        // 2) DB에서 회원 조회 (memberId/role 확보)
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. email=" + email));

        Long memberId = member.getMemberId();
        String role = normalizeRole(member.getRole()); // ROLE_ 접두어 보정

        log.info("[OAuth2 Success] memberId={}, email={}, role={}", memberId, member.getEmail(), role);

        // 3) JWT 발급 (memberId + role 사용)
        String accessToken  = jwtTokenProvider.createAccessToken(memberId, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);

// 4) RefreshToken을 DB에 저장 (업데이트)
        member.updateRefreshToken(refreshToken); // (Member 엔티티에 이 메서드가 있다고 가정)
        memberRepository.save(member);
        // ⬆️⬆️ 여기까지 추가 ⬆️⬆️

        // 5) 프론트로 리다이렉트 (딥링크 예시)
        String targetUrl = createRedirectUrl(accessToken, refreshToken);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);




    }

    // ROLE_ 접두어가 없으면 붙여주기
    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) return "ROLE_USER";
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }

    private String createRedirectUrl(String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString("guardpay://oauth-redirect")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build()
                .toUriString();
    }
}
