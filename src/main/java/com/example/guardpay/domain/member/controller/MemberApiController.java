package com.example.guardpay.domain.member.controller;


import com.example.guardpay.domain.member.dto.request.GoogleToken;
import com.example.guardpay.domain.member.dto.request.KakaoToken;
import com.example.guardpay.domain.member.dto.request.LoginRequest;
import com.example.guardpay.domain.member.dto.request.ReissueRequestDto;
import com.example.guardpay.domain.member.dto.request.SignupRequestDto;
import com.example.guardpay.domain.member.dto.response.AuthResponseDto;
import com.example.guardpay.domain.member.dto.response.JwtResponse;
import com.example.guardpay.domain.member.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.guardpay.domain.member.service.GoogleService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j; // ⬅️ [추가] 로그 사용

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "로그인/회원/인증", description = "인증관련 로그인/회원가입 로직")
public class MemberApiController {

    private final MemberSignService memberService; // 3. final 키워드 추가

    private final MemberLoginService memberLoginService;
    private final AuthService kakaoAuthService; // 신규유저, 기존 유저확인 로직(카카오)

    private final PasswordResetService passwordResetService; //

    private final checkEmailService checkEmailService;

    private final GoogleService googleService;

    //폼 회원가입
    @Operation(summary = "회원가입", description = "폼 회원가입")
    @PostMapping("/signup")
    // 4. 반환 타입을 Map<String, Object>로 수정
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignupRequestDto signUpRequestDto) {

        // 서비스 계층을 호출하여 회원가입 로직 수행
        memberService.signUp(signUpRequestDto);

        // 5. 성공 시 Map을 사용해 체계적인 JSON 응답 반환
        Map<String, Object> response = new HashMap<>();

        response.put("status", 201);
        response.put("message", "회원가입이 성공적으로 완료되었습니다.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//카카오 회원 가입
    @PostMapping("/kakao/login")
    @Operation(summary = "카카오 로그인", description = "카카오 소셜 로그인 및 회원가입")
    public ResponseEntity<AuthResponseDto> kakaoAuth(@RequestBody KakaoToken kakaoTokenDto) {
        // Service 로직을 호출하고 결과를 바로 반환
        AuthResponseDto responseDto = kakaoAuthService.loginOrSignup(kakaoTokenDto.getAccessToken());
        return ResponseEntity.ok(responseDto);
    }

    // 폼 로그인

    @PostMapping("/login") // ⬅️ @GetMapping이 아닌 @PostMapping
    @Operation(summary = "폼 로그인 ", description = "폼 로그인 jwt 토큰 발행 로직 ")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {

        // @RequestBody:
        // 클라이언트가 보낸 JSON 형식의 본문을 LoginRequest 객체로 변환해줍니다.

        // 1. 서비스 호출
        JwtResponse jwtResponse = memberLoginService.login(loginRequest);
        // 2. 응답 반환
        // HTTP 200 OK 상태 코드와 함께 JWT 토큰이 담긴 본문을 반환합니다.
        return ResponseEntity.ok(jwtResponse);
    }

    //임시 비번 메일 발송
    @PostMapping("/password-reset-request")
    @Operation(summary = "비밃번호 재설정", description = "비밀번호 재설정 로직, SMTP 라이블러리 이용 ")
    public ResponseEntity<Map<String, Object>> requestPasswordReset(
            @RequestBody Map<String, String> requestBody) {

        String email = requestBody.get("email");

        // 2. 서비스 로직 호출
        passwordResetService.issueTemporaryPassword(email);

        // 3. (보안상) 유저 존재 여부와 상관없이 항상 동일한 성공 응답을 보냄
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "비밀번호 재설정 메일이 발송되었습니다. 메일함을 확인해주세요.");

        return ResponseEntity.ok(response);
    }

    //이메일 중복 체크
    @Operation(summary = "이메일 중복 체크 ", description = "이메일 중복 체크 로직 ")
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, String>> checkEmail(@RequestParam("email") String email) {
        Map<String, String> response = new HashMap<>();

        boolean exists = checkEmailService.existsByEmail(email);

        if (exists) {
            response.put("message", "사용 불가 이메일입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            response.put("message", "사용 가능한 이메일입니다.");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/google/login")
    @Operation(summary = "구글 로그인", description = "구글 소셜 로그인 및 회원가입")
    public ResponseEntity<AuthResponseDto> googleLogin(@RequestBody GoogleToken googleTokenDto) {
        AuthResponseDto response = googleService.loginOrSignup(googleTokenDto.getIdToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody(required = false) ReissueRequestDto requestDto) {

        String refreshToken = null;

        // 1. 헤더에서 토큰 추출
        if (authHeader != null && authHeader.toLowerCase().startsWith("bearer ")) {
            refreshToken = authHeader.substring(7);
        }

        // 2. 헤더에 없으면 바디에서 추출
        if (refreshToken == null && requestDto != null && requestDto.getRefreshToken() != null) {
            refreshToken = requestDto.getRefreshToken();
        }

        // 3. 토큰이 아예 없는 경우
        if (refreshToken == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 401);
            response.put("message", "Refresh token is missing");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        try {
            // 4. MemberLoginService (또는 토큰 전담 서비스)의 재발급 로직 호출
            // (참고: MemberLoginService에 reissueToken(String refreshToken) 메서드를 구현해야 합니다)
            JwtResponse newTokens = memberLoginService.reissueToken(refreshToken);

            // 5. 성공 시 새 토큰 반환 (200 OK)
            return ResponseEntity.ok(newTokens);

        } catch (Exception e) {
            // 6. 갱신 실패 시 401 반환 (예: refreshToken 만료, 유효하지 않음)
            log.warn("[reissue] Token reissue failed: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", 401);
            // Flutter 로그에서 본 것과 동일한 형식의 응답을 반환합니다.
            response.put("message", "UNAUTHORIZED");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

}
