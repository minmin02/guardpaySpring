package com.example.guardpay.domain.member.service;


import com.example.guardpay.domain.member.converter.MemberConverter;
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
    private final MemberConverter memberConverter;


    // Spring Security의 PasswordEncoder를 주입받아야 하지만, 우선 개념 설명을 위해 로직만 구현합니다.
    // private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignupRequestDto requestDto) {
       // 비밀번호 받을 떄 인코더화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword()); // ✅ 암호화
        Member member= memberConverter.toEntity(requestDto,encodedPassword);
        memberRepository.save(member);
    }

}