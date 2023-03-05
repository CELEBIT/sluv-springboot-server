package com.sluv.server.global.jwt.exception;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends TokenException {

    private static final int ERROR_CODE = 4002;
    private static final String MESSAGE = "만료된 토큰입니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public ExpiredTokenException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
