package com.sluv.domain.closet.exception;

import com.sluv.common.exception.HttpStatusCode;

public class ClosetNotFoundException extends ClosetException {
    private static final int ERROR_CODE = 2018;
    private static final String MESSAGE = "존재하지 않는 Closet입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public ClosetNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
