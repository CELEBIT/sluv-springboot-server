package com.sluv.server.domain.celeb.exception;

import org.springframework.http.HttpStatus;

public class NotFoundCelebException extends CelebException {
    private static final int ERROR_CODE = 2002;
    private static final String MESSAGE = "존재하지 않는 셀럽입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public NotFoundCelebException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
