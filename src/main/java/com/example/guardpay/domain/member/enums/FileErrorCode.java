package com.example.guardpay.domain.member.enums;

import com.example.guardpay.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileErrorCode implements ResponseCode {

    FILE_EMPTY("FILE_400", "파일이 비어있습니다."),
    FILE_SIZE_EXCEEDED("FILE_401", "파일 크기는 5MB를 초과할 수 없습니다."),
    FILE_TYPE_INVALID("FILE_402", "이미지 파일만 업로드 가능합니다."),
    FILE_NAME_INVALID("FILE_403", "파일명이 유효하지 않습니다."),
    FILE_EXTENSION_NOT_SUPPORTED("FILE_405", "지원하지 않는 이미지 형식입니다. (jpg, jpeg, png, gif, webp만 가능)");



    private final String statusCode;
    private final String message;
}
