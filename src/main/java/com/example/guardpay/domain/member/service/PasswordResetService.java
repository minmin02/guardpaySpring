package com.example.guardpay.domain.member.service; // (패키지 경로는 맞게 수정)

import com.example.guardpay.domain.member.entity.Member;
import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.domain.member.exception.MemberException;
import com.example.guardpay.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PasswordResetService {

    // 1. 필요한 객체들 주입받기
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    /**
     * 임시 비밀번호 발급 로직
     */
    @Transactional
    public void issueTemporaryPassword(String email) {

        // 1. 이메일로 유저가 있는지 확인
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new MemberException(MemberErrorCode.EMAIL_NOT_FOUND));
        // 2. 임시 비밀번호 생성 (예: a1b2c3d4)
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);
        // 3. DB에 저장할 '암호화된' 임시 비밀번호 생성
        String encodedTempPassword = passwordEncoder.encode(tempPassword);
        member.updatePassword(encodedTempPassword); // (이 메서드는 Member 엔티티에 추가 필요)
        // 4. 사용자 이메일로 '원본' 임시 비밀번호 발송
        sendEmail(member.getEmail(), tempPassword);

    }

    /**
     * (private) 메일 발송 헬퍼 메서드
     */
    private void sendEmail(String toEmail, String tempPassword) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("[GuardPay] 임시 비밀번호 안내");
            mailMessage.setText(
                    "안녕하세요.\n\n" +
                            "요청하신 임시 비밀번호는 [ " + tempPassword + " ] 입니다.\n\n" +
                            "로그인 후 반드시 비밀번호를 변경해주세요.\n"
            );

            //  yml에 설정한 정보(username, password)로 메일을 발송
            //  package org.springframework.mail; <<  메일 보내주는 인터페이스
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            // (참고) 여기서 에러가 나도, DB의 비번은 이미 변경된 상태입니다.
            // 필요시 트랜잭션 롤백 처리를 고민할 수 있으나, 지금은 발송 실패만 로깅합니다.
        }
    }
}