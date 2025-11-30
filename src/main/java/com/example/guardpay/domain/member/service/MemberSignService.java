package com.example.guardpay.domain.member.service;


import com.example.guardpay.domain.member.dto.request.SignupRequestDto;
import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberSignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // ✅ 주입받기


    // Spring Security의 PasswordEncoder를 주입받아야 하지만, 우선 개념 설명을 위해 로직만 구현합니다.
    // private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignupRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword()); // ✅ 암호화


        // 비밀번호 암호화 로직 (실제로는 PasswordEncoder 사용)
        // String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword) // ✅ 암호화된 비번 저장
                .nickname(requestDto.getNickname())
                .role("ROLE_USER")
                .points(100000)
                .build();
        memberRepository.save(member);
    }

}