package com.sluv.server.domain.user.exception;

import org.springframework.http.HttpStatus;

public class TestException extends UserException {
    private static final String ERROR_CODE = "TOKEN-001";
    private static final String MESSAGE = "test Exception 입니다.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public TestException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
