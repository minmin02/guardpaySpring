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
import com.example.guardpay.global.jwt.MemberUserDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberLoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTtlSec;


    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {

        // 인증 -> Authentication 객체 반환
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // 인증된 객체 뽑아냄  (Object 타입이라 캐스팅 가능)
        MemberUserDetails userDetails = (MemberUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();
        String role = member.getRole();

        //토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(
                member.getMemberId(),
                member.getRole()
        );

        return JwtResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .expiresIn(accessTtlSec)
                .build();
    }


}
