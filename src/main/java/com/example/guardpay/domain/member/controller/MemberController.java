package com.example.guardpay.domain.member.controller;


import com.example.guardpay.domain.member.dto.request.*;
import com.example.guardpay.domain.member.dto.response.JwtResponse;
import com.example.guardpay.domain.member.dto.response.SignupResponseDto;
import com.example.guardpay.domain.member.service.*;
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
@RequestMapping("/api/v1/auth")
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


    // 폼 로그인
    @PostMapping("/login")
    @Operation(summary = "폼 로그인 ", description = "폼 로그인 jwt 토큰 발행 로직 ")
    public ApiResponse<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = memberLoginService.login(loginRequest);
        return ApiResponse.ok(jwtResponse);
    }

    //폼 회원가입
    @Operation(summary = "회원가입", description = "폼 회원가입")
    @PostMapping("/signup")
    public ApiResponse<SignupResponseDto> signUp(@RequestBody SignupRequestDto signUpRequestDto) {

        return ApiResponse.ok(memberSignService.signUp(signUpRequestDto));
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
            @AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = extractMemberId(userDetails);
        MemberDto.MemberInfoResponse response = memberService.getMemberInfo(memberId);
        return ApiResponse.ok(response);
    }

    /**
     * 프로필 수정 (닉네임, 비밀번호)
     */
    @PutMapping("/profile")
    public ApiResponse<MemberDto.UpdateProfileResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody MemberDto.UpdateProfileRequest request) {

        Long memberId = extractMemberId(userDetails);
        MemberDto.UpdateProfileResponse response = memberService.updateProfile(memberId, request);
        return ApiResponse.ok(response);
    }

    /**
     * 프로필 이미지 업데이트
     */
    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MemberDto.UpdateProfileImageResponse> updateProfileImage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("profileImage") MultipartFile profileImage) {

        Long memberId = extractMemberId(userDetails);
        MemberDto.UpdateProfileImageResponse response =
                memberService.updateProfileImage(memberId, profileImage);

        return ApiResponse.ok(response);
    }

    private Long extractMemberId(UserDetails userDetails) {
        return Long.parseLong(userDetails.getUsername());
    }


    /**
     * 프로필 이미지 삭제 (기본 이미지로 변경)
     * DELETE /api/members/profile/image
     */
    @DeleteMapping("/profile/image")
    public ApiResponse<Void> deleteProfileImage(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = extractMemberId(userDetails);
        memberService.deleteProfileImage(memberId);

        return ApiResponse.ok();
    }


    // 로그아웃 ->
    // 등급 조회
    @GetMapping("/me/grade")
    public ApiResponse< Map<String, String>> getMyGrade(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = extractMemberId(userDetails);
        Map<String, String> response=memberService.getGrade(memberId);
        return ApiResponse.ok(response);
    }





//    //카카오 회원 가입
//    @PostMapping("/kakao/login")
//    @Operation(summary = "카카오 로그인", description = "카카오 소셜 로그인 및 회원가입")
//    public ResponseEntity<AuthResponseDto> kakaoAuth(@RequestBody KakaoToken kakaoTokenDto) {
//        // Service 로직을 호출하고 결과를 바로 반환
//        AuthResponseDto responseDto = kakaoAuthService.loginOrSignup(kakaoTokenDto.getAccessToken());
//        return ResponseEntity.ok(responseDto);
//    }
//
//
//    @PostMapping("/google/login")
//    @Operation(summary = "구글 로그인", description = "구글 소셜 로그인 및 회원가입")
//    public ResponseEntity<AuthResponseDto> googleLogin(@RequestBody GoogleToken googleTokenDto) {
//        AuthResponseDto response = googleService.loginOrSignup(googleTokenDto.getIdToken());
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/reissue")
//    public ResponseEntity<?> reissueToken(
//            @RequestHeader(value = "Authorization", required = false) String authHeader,
//            @RequestBody(required = false) ReissueRequestDto requestDto) {
//
//        String refreshToken = null;
//
//        // 1. 헤더에서 토큰 추출
//        if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
//            refreshToken = authHeader.substring(7);
//        }
//
//        // 2. 헤더에 없으면 바디에서 추출
//        if (refreshToken == null && requestDto != null && requestDto.getRefreshToken() != null) {
//            refreshToken = requestDto.getRefreshToken();
//        }
//
//        // 3. 토큰이 아예 없는 경우
//        if (refreshToken == null) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("status", 401);
//            response.put("message", "Refresh token is missing");
//            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//        }
//
//        try {
//            // 4. MemberLoginService (또는 토큰 전담 서비스)의 재발급 로직 호출
//            // (참고: MemberLoginService에 reissueToken(String refreshToken) 메서드를 구현해야 합니다)
//            JwtResponse newTokens = memberLoginService.reissueToken(refreshToken);
//
//            // 5. 성공 시 새 토큰 반환 (200 OK)
//            return ResponseEntity.ok(newTokens);
//
//        } catch (Exception e) {
//            // 6. 갱신 실패 시 401 반환 (예: refreshToken 만료, 유효하지 않음)
//            log.warn("[reissue] Token reissue failed: {}", e.getMessage());
//            Map<String, Object> response = new HashMap<>();
//            response.put("status", 401);
//            // Flutter 로그에서 본 것과 동일한 형식의 응답을 반환합니다.
//            response.put("message", "UNAUTHORIZED");
//            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//        }
//    }

}
