package com.sluv.server.global.jwt.exception;

import org.springframework.http.HttpStatus;

public class NoAuthorizationTokenException extends TokenException {

    private static final int ERROR_CODE = 4000;
    private static final String MESSAGE = "토큰이 비어있습니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public NoAuthorizationTokenException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
