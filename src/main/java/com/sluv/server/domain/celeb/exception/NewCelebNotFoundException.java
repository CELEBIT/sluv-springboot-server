package com.sluv.server.domain.celeb.exception;

import org.springframework.http.HttpStatus;

public class NewCelebNotFoundException extends CelebException {
    private static final int ERROR_CODE = 2005;
    private static final String MESSAGE = "존재하지 않는 New 셀럽입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public NewCelebNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
