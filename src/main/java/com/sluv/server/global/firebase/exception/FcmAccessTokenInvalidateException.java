package com.sluv.server.global.firebase.exception;

import org.springframework.http.HttpStatus;

public class FcmAccessTokenInvalidateException extends FcmException {

    private static final int ERROR_CODE = 2028;
    private static final String MESSAGE = "잘못된 FCM 토큰입니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public FcmAccessTokenInvalidateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}