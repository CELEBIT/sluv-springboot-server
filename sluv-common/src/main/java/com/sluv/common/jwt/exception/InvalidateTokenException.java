package com.sluv.common.jwt.exception;

import com.sluv.common.exception.HttpStatusCode;

public class InvalidateTokenException extends TokenException {

    private static final int ERROR_CODE = 4001;
    private static final String MESSAGE = "잘못된 토큰입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.UNAUTHORIZED;

    public InvalidateTokenException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
