package com.sluv.domain.user.exception;

import com.sluv.common.exception.HttpStatusCode;

public class UserNoFCMException extends UserException {
    private static final int ERROR_CODE = 2030;
    private static final String MESSAGE = "FCM 토큰이 없습니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public UserNoFCMException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
