package com.sluv.server.global.jwt.exception;

import org.springframework.http.HttpStatus;

public class InvalidateTokenException extends TokenException {

    private static final String ERROR_CODE = "TOKEN-003";
    private static final String MESSAGE = "잘못된 토큰입니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public InvalidateTokenException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
