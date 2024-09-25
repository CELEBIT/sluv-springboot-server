package com.sluv.domain.auth.exception;

import com.sluv.common.exception.HttpStatusCode;

public class UserBlockedException extends AuthException {
    private static final int ERROR_CODE = 2023;
    private static final String MESSAGE = "정지된 유저입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public UserBlockedException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
