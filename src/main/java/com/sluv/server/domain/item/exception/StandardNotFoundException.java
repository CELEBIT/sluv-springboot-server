package com.sluv.server.domain.item.exception;

import org.springframework.http.HttpStatus;

public class StandardNotFoundException extends ItemException {
    private static final int ERROR_CODE = 2021;
    private static final String MESSAGE = "존재하지 않는 Standard입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public StandardNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
