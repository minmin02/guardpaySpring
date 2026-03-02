package com.example.guardpay.domain.member.converter;

import com.example.guardpay.domain.member.dto.request.MemberDto;
import com.example.guardpay.domain.member.dto.request.SignupRequestDto;
import com.example.guardpay.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    /**
     * Member 엔티티를 MemberInfoResponse DTO로 변환
     */
    public MemberDto.MemberInfoResponse toMemberInfoResponse(Member member) {

        if (member == null) {
            return null;
        }
        return MemberDto.MemberInfoResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl()) // 추가
                .points(member.getPoints())
                .exp(member.getExp())
                .fontSize(member.getFontSize())
                .grade(member.getGrade()) // 추가 (필요시)
                .status(member.getStatus()) // 추가 (필요시)
                .provider(member.getProvider()) // 추가 (필요시)
                .role(member.getRole()) // 추가 (필요시)
                .createdAt(member.getCreatedAt()) // 추가 (필요시)
                .updatedAt(member.getUpdatedAt()) // 추가 (필요시)
                .build();
    }

    /**
     * Member 엔티티를 UpdateProfileResponse DTO로 변환
     */
    public MemberDto.UpdateProfileResponse toUpdateProfileResponse(Member member, String message) {
        if (member == null) {
            return null;
        }

        return MemberDto.UpdateProfileResponse.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .message(message)
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    /**
     * Member 엔티티를 UpdateProfileImageResponse DTO로 변환
     */
    public MemberDto.UpdateProfileImageResponse toUpdateProfileImageResponse(Member member, String message) {
        if (member == null) {
            return null;
        }

        return MemberDto.UpdateProfileImageResponse.builder()
                .memberId(member.getMemberId())
                .profileImageUrl(member.getProfileImageUrl())
                .message(message)
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    // DTO -> Entity
    public Member toEntity(SignupRequestDto requestDto, String encodePassword) {
        return Member.builder()
                .email(requestDto.getEmail())
                .password(encodePassword)
                .nickname(requestDto.getNickname())
                .role("ROLE_USER")
                .points(100000)
                .build();
    }


}