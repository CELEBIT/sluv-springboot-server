package com.sluv.server.domain.user.exception;

import org.springframework.http.HttpStatus;

public class UserReportDuplicateException extends UserException {
    private static final int ERROR_CODE = 2010;
    private static final String MESSAGE = "중복된 유저 신고입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public UserReportDuplicateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
