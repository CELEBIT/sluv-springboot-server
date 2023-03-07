package com.sluv.server.global.jwt.exception;

import org.springframework.http.HttpStatus;

public class InvalidateTokenException extends TokenException {

    private static final int ERROR_CODE = 4001;
    private static final String MESSAGE = "잘못된 토큰입니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public InvalidateTokenException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
