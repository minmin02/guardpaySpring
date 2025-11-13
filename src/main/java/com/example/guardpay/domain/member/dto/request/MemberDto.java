package com.example.guardpay.domain.member.dto.request;

import com.example.guardpay.domain.member.data.Grade;
import lombok.*;

import java.time.LocalDateTime;

public class MemberDto {

    /**
     * 프로필 수정 요청 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileRequest {
        private String nickname;
        private String password;
        private String currentPassword;
    }

    /**
     * 프로필 수정 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileResponse {
        private Long memberId;
        private String nickname;
        private String message;
        private LocalDateTime updatedAt;
    }

    /**
     * 회원 정보 조회 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfoResponse {
        private Long memberId;
        private String email;
        private String nickname;
        private String profileImageUrl;
        private int points;
        private Grade grade;
        private String status;
        private int exp;
        private int fontSize;
        private String provider;
        private String role;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    /**
     * 프로필 이미지 수정 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileImageResponse {
        private Long memberId;
        private String profileImageUrl;
        private String message;
        private LocalDateTime updatedAt;
    }
}