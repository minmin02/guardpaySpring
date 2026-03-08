package com.example.guardpay.global.auth;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.domain.member.exception.MemberException;
import com.example.guardpay.domain.member.repository.MemberRepository;
import com.example.guardpay.global.exception.MemberNotFoundException;
import com.example.guardpay.global.jwt.MemberUserDetails;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        Member member=memberRepository.findByEmail(email).orElseThrow(()-> new MemberException(MemberErrorCode.EMAIL_NOT_FOUND));
        return new MemberUserDetails(member);
    }
}
