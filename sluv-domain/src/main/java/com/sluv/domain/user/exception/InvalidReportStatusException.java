package com.sluv.domain.user.exception;

import com.sluv.common.exception.ApplicationException;
import com.sluv.common.exception.HttpStatusCode;

public class InvalidReportStatusException extends ApplicationException {

    private static final int ERROR_CODE = 2002;
    private static final String MESSAGE = "잘못된 UserReportStatus 입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public InvalidReportStatusException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}