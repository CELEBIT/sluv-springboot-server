package com.sluv.server.domain.alarm.exception;

import org.springframework.http.HttpStatus;

public class AlarmNotFoundException extends AlarmException {
    private static final int ERROR_CODE = 7000;
    private static final String MESSAGE = "존재하지 않는 알람입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public AlarmNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
