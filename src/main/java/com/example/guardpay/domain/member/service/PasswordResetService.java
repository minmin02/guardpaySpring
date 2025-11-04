package com.example.guardpay.domain.member.service; // (패키지 경로는 맞게 수정)

import com.example.guardpay.domain.member.entity.Member;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    // 1. 필요한 객체들 주입받기
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender; // ⬅️ yml 설정으로 자동 생성됨

    /**
     * 임시 비밀번호 발급 로직
     */
    @Transactional
    public void issueTemporaryPassword(String email) {

        // 1. 이메일로 유저가 있는지 확인
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        // 2. 유저가 없으면? (보안상) 아무것도 안 하고 조용히 종료.
        if (memberOptional.isEmpty()) {
            log.info("비밀번호 재설정 요청: 존재하지 않는 이메일 {}", email);
            return;
        }

        Member member = memberOptional.get();

        // 3. 임시 비밀번호 생성 (예: a1b2c3d4)
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        // 4. DB에 저장할 '암호화된' 임시 비밀번호 생성
        String encodedTempPassword = passwordEncoder.encode(tempPassword);

        // 5. DB에 유저 비번을 '암호화된' 값으로 업데이트
        member.updatePassword(encodedTempPassword); // (이 메서드는 Member 엔티티에 추가 필요)

        // 6. 사용자 이메일로 '원본' 임시 비밀번호 발송
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

            // ⬇️ yml에 설정한 정보(username, password)로 메일을 발송합니다.
            javaMailSender.send(mailMessage);

            log.info("임시 비밀번호 메일 발송 성공: {}", toEmail);
        } catch (Exception e) {
            log.error("메일 발송 실패: {}", e.getMessage());
            // (참고) 여기서 에러가 나도, DB의 비번은 이미 변경된 상태입니다.
            // 필요시 트랜잭션 롤백 처리를 고민할 수 있으나, 지금은 발송 실패만 로깅합니다.
        }
    }
}