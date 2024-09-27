package com.sluv.domain.celeb.exception;

import com.sluv.common.exception.HttpStatusCode;

public class CelebNotFoundException extends CelebException {
    private static final int ERROR_CODE = 2002;
    private static final String MESSAGE = "존재하지 않는 셀럽입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public CelebNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
