package com.sluv.domain.admin.exception;

import com.sluv.common.exception.HttpStatusCode;

public class AdminPasswordNotMatchException extends AdminException {
    private static final int ERROR_CODE = 2027;
    private static final String MESSAGE = "Admin 비밀번호가 일치하지 않습니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public AdminPasswordNotMatchException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
