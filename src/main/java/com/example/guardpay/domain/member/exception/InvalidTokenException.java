package com.example.guardpay.domain.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // 이 예외가 발생하면 401을 반환
public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(String message) {
    super(message);
  }

  public InvalidTokenException() {
    super("유효하지 않거나 만료된 토큰입니다.");
  }
}
