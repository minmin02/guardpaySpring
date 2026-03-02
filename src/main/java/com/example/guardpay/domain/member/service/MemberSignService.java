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
    private final PasswordEncoder passwordEncoder;
    private final MemberConverter memberConverter;

    @Transactional
    public void signUp(SignupRequestDto requestDto) {
       // 비밀번호 받을 떄 인코더화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        // DTO -> 엔티티
        Member member= memberConverter.toEntity(requestDto,encodedPassword);
        memberRepository.save(member);
    }

}