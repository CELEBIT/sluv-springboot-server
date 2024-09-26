package com.sluv.domain.user.exception;

import com.sluv.common.exception.HttpStatusCode;

public class UserReportDuplicateException extends UserException {
    private static final int ERROR_CODE = 2010;
    private static final String MESSAGE = "중복된 유저 신고입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public UserReportDuplicateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
