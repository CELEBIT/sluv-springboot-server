package com.sluv.domain.user.exception;

import com.sluv.common.exception.HttpStatusCode;

public class UserReportNotFoundException extends UserException {
    private static final int ERROR_CODE = 2001;
    private static final String MESSAGE = "존재하지 않는 유저 신고입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public UserReportNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}