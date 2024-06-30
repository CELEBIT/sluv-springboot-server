package com.sluv.server.global.firebase.exception;

import org.springframework.http.HttpStatus;

public class FcmConnectException extends FcmException {

    private static final int ERROR_CODE = 2022;
    private static final String MESSAGE = "Firebase connection 에러입니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public FcmConnectException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}