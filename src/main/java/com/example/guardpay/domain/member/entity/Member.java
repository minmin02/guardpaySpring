package com.example.guardpay.domain.member.entity;

import com.example.guardpay.domain.member.data.Grade;
import com.example.guardpay.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

@Id
@GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long memberId;


    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false, length = 50)
    private String nickname; // 닉네임


    private int points; // 포인트

    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private Grade grade;

    @Column(length = 20)
    private String status; // 회원상태

    private int exp; // 경험치

    @Column(name = "font_size")
    private int fontSize; // 폰트크기

    @CreatedDate // 엔터티 생성 시 시간 자동 저장
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 회원가입일시

    @LastModifiedDate // 엔터티 수정 시 시간 자동 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 회원정보수정일시

    @Column(length = 20)
    private String provider; // 소셜 로그인 제공자

    @Column(name = "provider_id")
    private String providerId; // 소셜ID

    @Column(length = 20)
    private String role; // 사용자 권한

    @Column(length = 512) // ⬅️ [추가] 리프레시 토큰 저장 필드 (넉넉하게)
    private String refreshToken;

    @Column(length = 512)
    private String profileImageUrl;



    @Builder
    public Member(String email, String password, String nickname, String role, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;

        this.points = 0;
        this.grade = Grade.주의_필요;
        this.status = "ACTIVE"; // 예: 활성 상태를 기본값으로 지정
        this.exp = 0;
        this.fontSize = 16; // 예: 기본 폰트 크기

        this.role = "ROLE_USER";
        this.provider = provider;
        this.providerId = providerId;
    }

    //소셜 회원가입 메소드
    public static Member createSocialMember(String email, String nickname, String provider, String providerId) {
        Member member = new Member();
        member.email = email;
        member.nickname = nickname;

        member.provider = provider;
        member.providerId = providerId;



        // 소셜 로그인 사용자는 비밀번호를 사용하지 않으므로, 보안을 위해 임의의 값을 할당
        member.password = UUID.randomUUID().toString();
        member.role = "ROLE_USER"; // 기본 권한 부여
        // ... grade, points 등 기타 필드 기본값 설정 ...
        member.points = 0;
        member.grade = Grade.주의_필요;
        member.status = "ACTIVE"; // 예: 활성 상태를 기본값으로 지정
        member.exp = 0;
        member.fontSize = 16;

        return member;
    }

    public Member updateSocialProfile(String nickname) {
        this.nickname = nickname;
        return this;
    }
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String newPasswordHash) {
        this.password = newPasswordHash;
    }
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 프로필 일괄 수정
    public void updateProfile(String nickname, String passwordHash, Grade grade) {
        if (nickname != null && !nickname.trim().isEmpty()) {
            this.nickname = nickname;
        }
        if (passwordHash != null && !passwordHash.trim().isEmpty()) {
            this.password = passwordHash;
        }
    }


    public void addExp(int expToAdd) {
        this.exp += expToAdd;
        this.points += expToAdd; // 포인트도 함께 적립할 경우
    }


    // 프로필 이미지만 업데이트
    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setGrade(String finalGrade) {
        this.grade = Grade.valueOf(finalGrade.replace(" ", "_"));
    }

    public int getBalance() {return this.points;}

    public void setBalance(int updatedBalance) {this.points = updatedBalance;}


}
