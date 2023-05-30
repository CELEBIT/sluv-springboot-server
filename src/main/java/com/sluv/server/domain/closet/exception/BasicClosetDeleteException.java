package com.sluv.server.domain.closet.exception;

import org.springframework.http.HttpStatus;

public class BasicClosetDeleteException extends ClosetException {
    private static final int ERROR_CODE = 2019;
    private static final String MESSAGE = "기본 Closet은 삭제할 수 없습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BasicClosetDeleteException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
