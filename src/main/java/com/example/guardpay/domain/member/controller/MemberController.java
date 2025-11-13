package com.example.guardpay.domain.member.controller;

import com.example.guardpay.domain.member.dto.request.MemberDto;
import com.example.guardpay.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 정보 조회
     * GET /api/members/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<MemberDto.MemberInfoResponse> getMemberInfo(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = extractMemberId(userDetails);
        MemberDto.MemberInfoResponse response = memberService.getMemberInfo(memberId);

        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 수정 (닉네임, 비밀번호)
     * PUT /api/members/profile
     */
    @PutMapping("/profile")
    public ResponseEntity<MemberDto.UpdateProfileResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody MemberDto.UpdateProfileRequest request) {

        Long memberId = extractMemberId(userDetails);
        MemberDto.UpdateProfileResponse response = memberService.updateProfile(memberId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 이미지만 업데이트
     * PUT /api/members/profile/image
     */
    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberDto.UpdateProfileImageResponse> updateProfileImage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("profileImage") MultipartFile profileImage) {

        Long memberId = extractMemberId(userDetails);
        MemberDto.UpdateProfileImageResponse response =
                memberService.updateProfileImage(memberId, profileImage);

        return ResponseEntity.ok(response);
    }

    /**
     * 프로필 이미지 삭제 (기본 이미지로 변경)
     * DELETE /api/members/profile/image
     */
    @DeleteMapping("/profile/image")
    public ResponseEntity<Void> deleteProfileImage(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = extractMemberId(userDetails);
        memberService.deleteProfileImage(memberId);

        return ResponseEntity.noContent().build();
    }

    private Long extractMemberId(UserDetails userDetails) {
        // 방법 1: username이 memberId인 경우
        return Long.parseLong(userDetails.getUsername());
    }

    // 로그아웃 ->


}