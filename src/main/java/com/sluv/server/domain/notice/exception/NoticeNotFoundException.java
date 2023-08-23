package com.sluv.server.domain.notice.exception;

import org.springframework.http.HttpStatus;

public class NoticeNotFoundException extends NoticeException{

    private static final int ERROR_CODE = 2024;
    private static final String MESSAGE = "존재하지 않는 Notice입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public NoticeNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
