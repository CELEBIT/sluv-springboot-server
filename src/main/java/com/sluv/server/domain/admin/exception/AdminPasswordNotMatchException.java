package com.sluv.server.domain.admin.exception;

import org.springframework.http.HttpStatus;

public class AdminPasswordNotMatchException extends AdminException {
    private static final int ERROR_CODE = 2027;
    private static final String MESSAGE = "Admin 비밀번호가 일치하지 않습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public AdminPasswordNotMatchException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
