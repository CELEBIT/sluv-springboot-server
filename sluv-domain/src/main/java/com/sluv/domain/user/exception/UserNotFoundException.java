package com.sluv.domain.user.exception;

import com.sluv.common.exception.HttpStatusCode;

public class UserNotFoundException extends UserException {
    private static final int ERROR_CODE = 2000;
    private static final String MESSAGE = "존재하지 않는 유저입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public UserNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
