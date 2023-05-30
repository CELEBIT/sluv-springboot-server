package com.sluv.server.domain.closet.exception;

import org.springframework.http.HttpStatus;

public class ClosetNotFoundException extends ClosetException {
    private static final int ERROR_CODE = 2018;
    private static final String MESSAGE = "존재하지 않는 Closet입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public ClosetNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
