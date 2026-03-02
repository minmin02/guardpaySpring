package com.example.guardpay.domain.member.service;

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections; // authorities를 위해 import

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * Spring Security가 인증 시 사용할 메서드입니다.
     * @param email (Username)
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다: " + email));

        // Spring Security가 이해할 수 있는 UserDetails 객체로 변환
        // 여기서는 간단히 Spring Security의 User 객체를 사용합니다.
        // member.getRoles() 등을 사용하여 권한 목록을 동적으로 설정해야 합니다.
        return new User(
                member.getEmail(),
                member.getPassword(), // DB에 저장된 해시된 비밀번호
                Collections.emptyList() // ➡️ 실제로는 member.getRoles()에서 권한(Authority)을 가져와야 함
        );
    }


}