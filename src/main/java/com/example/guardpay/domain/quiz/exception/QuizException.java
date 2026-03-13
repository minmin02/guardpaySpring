package com.example.guardpay.domain.quiz.exception;

import com.example.guardpay.domain.quiz.enums.QuizErrorCode;
import com.example.guardpay.domain.video.enums.VideoErrorCode;
import com.example.guardpay.global.exception.CustomException;

public class QuizException extends CustomException {

    public QuizException(QuizErrorCode errorCode) {
        super(errorCode);
    }

}
