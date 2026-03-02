package com.example.guardpay.domain.member.service;

import com.example.guardpay.domain.member.dto.request.LoginRequest;
import com.example.guardpay.domain.member.dto.response.JwtResponse;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.exception.InvalidTokenException;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.global.exception.InvalidPasswordException;
import com.example.guardpay.global.exception.MemberNotFoundException;
// ⬇️ [추가] 토큰 관련 예외 import

import com.example.guardpay.global.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberLoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTtlSec;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTtlSec;

    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {


        //이메일로 회원 객체 뽑아옴
        Member member = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        //비밀번호 검증
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new InvalidPasswordException();
        }

        // 3) 역할 정규화 (선택) — DB가 "USER"면 "ROLE_USER"로 정규화
        String role = normalizeRole(member.getRole()); // ⬅️ [수정] 메서드 호출로 변경

        // 4) 토큰 발급 (반드시 같은 JwtTokenProvider 사용)
        String accessToken  = jwtTokenProvider.createAccessToken(member.getMemberId(), role);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getMemberId());

        // 5) [수정] 발급된 RefreshToken을 DB에 저장
        member.updateRefreshToken(refreshToken);
        // (login 메서드에 @Transactional이 있으므로 save 호출은 생략 가능)

        log.info("Access Token length: {}", accessToken.length());
        log.info("Refresh Token length: {}", refreshToken.length());

        return JwtResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessTtlSec)           
                .refreshExpiresIn(refreshTtlSec)
                .build();
    }


    // ⬇️⬇️ [신규 메서드 추가] ⬇️⬇️
    /**
     * 토큰 재발급 (Refresh Token Rotation 적용)
     * @param refreshToken
     * @return
     */
    @Transactional
    public JwtResponse reissueToken(String refreshToken) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            log.warn("[reissue] 1. Invalid JWT token");
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }

        // 2. Token Type 검증 (refresh 타입이어야 함)
        String type = jwtTokenProvider.getTokenType(refreshToken);
        if (type == null || !"refresh".equalsIgnoreCase(type)) {
            log.warn("[reissue] 2. Token type invalid: {}", type);
            throw new InvalidTokenException("토큰 타입이 유효하지 않습니다.");
        }

        // 3. 토큰에서 memberId 추출
        Long memberId = jwtTokenProvider.getMemberId(refreshToken);
        if (memberId == null) {
            log.warn("[reissue] 3. Member ID not found in token");
            throw new InvalidTokenException("토큰에서 사용자 ID를 찾을 수 없습니다.");
        }

        // 4. DB에서 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[reissue] 4. Member not found in DB: id={}", memberId);
                    return new MemberNotFoundException("사용자를 찾을 수 없습니다.");
                });

        // 5. DB의 Refresh Token과 일치하는지 확인 (탈취 방지)
        String dbRefreshToken = member.getRefreshToken();
        if (dbRefreshToken == null || !dbRefreshToken.equals(refreshToken)) {
            log.warn("[reissue] 5. DB token mismatch. (DB={} / REQ={})", dbRefreshToken, refreshToken);
            // (보안) 탈취 가능성이 있으므로 DB의 토큰을 만료시킴
            member.updateRefreshToken(null);
            throw new InvalidTokenException("토큰이 일치하지 않습니다. 다시 로그인해주세요.");
        }

        // 6. 새 토큰 발급 (Access, Refresh 둘 다)
        String role = normalizeRole(member.getRole());
        String newAccessToken  = jwtTokenProvider.createAccessToken(member.getMemberId(), role);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(member.getMemberId());

        // 7. 새 Refresh Token을 DB에 저장 (Rotation)
        member.updateRefreshToken(newRefreshToken);

        log.info("[reissue] Token reissued successfully for memberId={}", memberId);

        // 8. 새 토큰 정보 반환
        return JwtResponse.builder()
                .tokenType("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(accessTtlSec)
                .refreshExpiresIn(refreshTtlSec)
                .build();
    }

    // ⬇️ [추가] 역할 정규화 private 메서드
    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) return "ROLE_USER"; // 기본값
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }
    // ⬆️⬆️ [신규 메서드 추가 완료] ⬆️⬆️
}
