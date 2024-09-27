package com.sluv.domain.alarm.exception;

import com.sluv.common.exception.HttpStatusCode;

public class AlarmAccessDeniedException extends AlarmException {
    private static final int ERROR_CODE = 7001;
    private static final String MESSAGE = "알람의 사용자와 일치하지 않습니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public AlarmAccessDeniedException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
