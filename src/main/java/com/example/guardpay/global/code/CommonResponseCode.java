package com.example.guardpay.global.code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum CommonResponseCode implements ResponseCode{

    // 성공
    OK("200", "SUCCESS"),
    CREATED("201", "CREATED"),

    // 클라이언트 에러
    BAD_REQUEST("400", "BAD REQUEST"),
    UNAUTHORIZED("401", "UNAUTHORIZED"),
    FORBIDDEN("403", "FORBIDDEN"),
    NOT_FOUND("404", "NOT FOUND"),

    // 서버 에러
    INTERNAL_SERVER_ERROR("500", "INTERNAL SERVER ERROR");

    private final String statusCode;
    private final String message;

}
