package com.sluv.server.global.firebase.exception;

import com.sluv.server.global.jwt.exception.TokenException;
import org.springframework.http.HttpStatus;

public class FCMAccessTokenInvalidateException extends TokenException {

    private static final int ERROR_CODE = 4003;
    private static final String MESSAGE = "잘못된 FCM 토큰입니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public FCMAccessTokenInvalidateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
