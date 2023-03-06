package com.sluv.server.domain.auth.exception;

import org.springframework.http.HttpStatus;

public class NoSupportSocialTypeException extends AuthException {
    private static final int ERROR_CODE = 2001;
    private static final String MESSAGE = "지원하지 않는 SnsType입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public NoSupportSocialTypeException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
