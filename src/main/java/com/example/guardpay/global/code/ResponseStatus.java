//package com.example.guardpay.global.code;
//
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//
//@Getter
//@RequiredArgsConstructor
//public enum ResponseStatus {
//
//    // 200: 요청 성공
//    SUCCESS(HttpStatus.OK, "요청에 성공하였습니다."),
//
//    // 회원가입 관련
//    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다."),
//
//    // 400: 잘못된 요청
//    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
//
//    // --- 여기에 필요한 다른 상태 코드들을 추가 ---
//    // 예: LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
//    //     USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
//
//    private final HttpStatus httpStatus;
//    private final String message;
//
//
//}