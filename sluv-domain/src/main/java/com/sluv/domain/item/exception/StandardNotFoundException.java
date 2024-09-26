package com.sluv.domain.item.exception;

import com.sluv.common.exception.HttpStatusCode;

public class StandardNotFoundException extends ItemException {
    private static final int ERROR_CODE = 2021;
    private static final String MESSAGE = "존재하지 않는 Standard입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public StandardNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
