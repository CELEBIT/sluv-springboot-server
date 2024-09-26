package com.sluv.common.jwt.exception;

import com.sluv.common.exception.HttpStatusCode;

public class NoAuthorizationTokenException extends TokenException {

    private static final int ERROR_CODE = 4000;
    private static final String MESSAGE = "토큰이 비어있습니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.UNAUTHORIZED;

    public NoAuthorizationTokenException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
