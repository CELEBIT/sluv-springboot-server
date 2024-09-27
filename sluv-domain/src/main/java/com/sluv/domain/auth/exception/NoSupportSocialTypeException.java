package com.sluv.domain.auth.exception;

import com.sluv.common.exception.HttpStatusCode;

public class NoSupportSocialTypeException extends AuthException {
    private static final int ERROR_CODE = 2001;
    private static final String MESSAGE = "지원하지 않는 SnsType입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public NoSupportSocialTypeException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
