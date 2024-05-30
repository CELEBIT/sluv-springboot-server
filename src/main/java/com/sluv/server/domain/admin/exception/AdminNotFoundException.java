package com.sluv.server.domain.admin.exception;

import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends AdminException {
    private static final int ERROR_CODE = 2026;
    private static final String MESSAGE = "존재하지 않는 Admin입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public AdminNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
