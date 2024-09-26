package com.sluv.domain.celeb.exception;

import com.sluv.common.exception.HttpStatusCode;

public class NewCelebNotFoundException extends CelebException {
    private static final int ERROR_CODE = 2005;
    private static final String MESSAGE = "존재하지 않는 New 셀럽입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public NewCelebNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
