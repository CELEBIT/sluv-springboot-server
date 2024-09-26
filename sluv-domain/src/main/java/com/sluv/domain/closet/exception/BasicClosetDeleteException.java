package com.sluv.domain.closet.exception;

import com.sluv.common.exception.HttpStatusCode;

public class BasicClosetDeleteException extends ClosetException {
    private static final int ERROR_CODE = 2019;
    private static final String MESSAGE = "기본 Closet은 삭제할 수 없습니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public BasicClosetDeleteException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
