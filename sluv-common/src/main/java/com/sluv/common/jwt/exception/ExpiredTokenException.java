package com.sluv.common.jwt.exception;

import com.sluv.common.exception.HttpStatusCode;

public class ExpiredTokenException extends TokenException {

    private static final int ERROR_CODE = 4002;
    private static final String MESSAGE = "만료된 토큰입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.UNAUTHORIZED;

    public ExpiredTokenException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
