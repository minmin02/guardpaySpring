package com.example.guardpay.domain.member.controller;


import com.example.guardpay.domain.member.dto.request.*;
import com.example.guardpay.domain.member.dto.response.JwtResponse;
import com.example.guardpay.domain.member.dto.response.SignupResponseDto;
import com.example.guardpay.domain.member.service.*;
import com.example.guardpay.global.jwt.MemberUserDetails;
import com.example.guardpay.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import lombok.extern.slf4j.Slf4j; // ⬅️ [추가] 로그 사용
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name = "로그인/회원/인증", description = "인증관련 로그인/회원가입 로직")
public class MemberController {

    private final MemberSignService memberSignService;
    private final MemberLoginService memberLoginService;
    private final MemberService memberService;
    private final PasswordResetService passwordResetService;
    private final checkEmailService checkEmailService;




    //이메일 중복 체크
    @Operation(summary = "이메일 중복 체크 ", description = "이메일 중복 체크 로직 ")
    @GetMapping("/check-email")
    public ApiResponse checkEmail(@RequestParam("email") String email) {
        checkEmailService.checkEmailDuplicate(email);
        return ApiResponse.ok();
    }



    //임시 비번 메일 발송
    @PostMapping("/password-reset-request")
    @Operation(summary = "비밃번호 재설정", description = "비밀번호 재설정 로직, SMTP 라이블러리 이용 ")
    public ApiResponse<Void> requestPasswordReset(
            @RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        passwordResetService.issueTemporaryPassword(email);
     return ApiResponse.ok();
    }


    /**
     * 회원 정보 조회
     */
    @GetMapping("/profile")
    public ApiResponse<MemberDto.MemberInfoResponse> getMemberInfo(
            @AuthenticationPrincipal MemberUserDetails userDetails) {
        Long memberId = userDetails.getMember().getMemberId();
        MemberDto.MemberInfoResponse response = memberService.getMemberInfo(memberId);
        return ApiResponse.ok(response);
    }

    /**
     * 프로필 수정 (닉네임, 비밀번호)
     */
    @PutMapping("/profile")
    public ApiResponse<MemberDto.UpdateProfileResponse> updateProfile(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @RequestBody MemberDto.UpdateProfileRequest request) {

        Long memberId = userDetails.getMember().getMemberId();
        MemberDto.UpdateProfileResponse response = memberService.updateProfile(memberId, request);
        return ApiResponse.ok(response);
    }

    /**
     * 프로필 이미지 업데이트
     */
    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MemberDto.UpdateProfileImageResponse> updateProfileImage(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @RequestPart("profileImage") MultipartFile profileImage) {

        Long memberId = userDetails.getMember().getMemberId();
        MemberDto.UpdateProfileImageResponse response =
                memberService.updateProfileImage(memberId, profileImage);

        return ApiResponse.ok(response);
    }




    /**
     * 프로필 이미지 삭제 (기본 이미지로 변경)
     * DELETE /api/members/profile/image
     */
    @DeleteMapping("/profile/image")
    public ApiResponse<Void> deleteProfileImage(
            @AuthenticationPrincipal MemberUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        memberService.deleteProfileImage(memberId);

        return ApiResponse.ok();
    }


    // 로그아웃 ->
    // 등급 조회
    @GetMapping("/me/grade")
    public ApiResponse< Map<String, String>> getMyGrade(@AuthenticationPrincipal MemberUserDetails userDetails) {
        Long memberId = userDetails.getMember().getMemberId();
        Map<String, String> response=memberService.getGrade(memberId);
        return ApiResponse.ok(response);
    }

}
