package com.sluv.domain.notice.exception;

import com.sluv.common.exception.HttpStatusCode;

public class NoticeNotFoundException extends NoticeException {

    private static final int ERROR_CODE = 2024;
    private static final String MESSAGE = "존재하지 않는 Notice입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public NoticeNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
