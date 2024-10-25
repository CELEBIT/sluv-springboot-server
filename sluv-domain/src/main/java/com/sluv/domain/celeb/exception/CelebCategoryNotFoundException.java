package com.sluv.domain.celeb.exception;

import com.sluv.common.exception.HttpStatusCode;

public class CelebCategoryNotFoundException extends CelebException {
    private static final int ERROR_CODE = 2030;
    private static final String MESSAGE = "존재하지 않는 셀럽 카테고리입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public CelebCategoryNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
