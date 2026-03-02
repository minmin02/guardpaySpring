//package com.example.guardpay.global.dto;
//
//
//import com.example.guardpay.global.code.ResponseStatus;
//import lombok.Getter;
//
//@Getter
//public class ApiResponse<T> {
//
//    private final int status; // HTTP 상태 코드
//    private final String message;
//    private final T data;
//
//    private ApiResponse(ResponseStatus responseStatus, T data) {
//        this.status = responseStatus.getHttpStatus().value(); // 200, 404 등 숫자 코드
//        this.message = responseStatus.getMessage();
//        this.data = data;
//    }
//
//    // 성공 시 응답 생성 (데이터 포함)
//    public static <T> ApiResponse<T> onSuccess(ResponseStatus responseStatus, T data) {
//        return new ApiResponse<>(responseStatus, data);
//    }
//
//    // 성공 시 응답 생성 (데이터 없음)
//    public static <T> ApiResponse<T> onSuccess(ResponseStatus responseStatus) {
//        return new ApiResponse<>(responseStatus, null);
//    }
//
//    // 실패 시 응답 생성 (필요에 따라 추가)
//    public static <T> ApiResponse<T> onFailure(ResponseStatus responseStatus, T data) {
//        return new ApiResponse<>(responseStatus, data);
//    }
//
//
//}