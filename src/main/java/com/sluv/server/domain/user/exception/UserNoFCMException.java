package com.sluv.server.domain.user.exception;

import org.springframework.http.HttpStatus;

public class UserNoFCMException extends UserException {
    private static final int ERROR_CODE = 2030;
    private static final String MESSAGE = "FCM 토큰이 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public UserNoFCMException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
