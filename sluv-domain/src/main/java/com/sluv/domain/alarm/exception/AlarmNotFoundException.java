package com.sluv.domain.alarm.exception;

import com.sluv.common.exception.HttpStatusCode;

public class AlarmNotFoundException extends AlarmException {
    private static final int ERROR_CODE = 7000;
    private static final String MESSAGE = "존재하지 않는 알람입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public AlarmNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
