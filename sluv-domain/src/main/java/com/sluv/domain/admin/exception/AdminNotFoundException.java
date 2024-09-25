package com.sluv.domain.admin.exception;

import com.sluv.common.exception.HttpStatusCode;

public class AdminNotFoundException extends AdminException {
    private static final int ERROR_CODE = 2026;
    private static final String MESSAGE = "존재하지 않는 Admin입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public AdminNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
