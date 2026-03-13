package com.example.guardpay.domain.video.exception;

import com.example.guardpay.domain.video.enums.VideoErrorCode;
import com.example.guardpay.global.exception.CustomException;

public class VideoException extends CustomException {

    public VideoException(VideoErrorCode errorCode) {
        super(errorCode);
    }

}
