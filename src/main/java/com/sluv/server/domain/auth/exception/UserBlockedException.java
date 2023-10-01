package com.sluv.server.domain.auth.exception;

import org.springframework.http.HttpStatus;

public class UserBlockedException extends AuthException {
    private static final int ERROR_CODE = 2023;
    private static final String MESSAGE = "정지된 유저입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public UserBlockedException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
