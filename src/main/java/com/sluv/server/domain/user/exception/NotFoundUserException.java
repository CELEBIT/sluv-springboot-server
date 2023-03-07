package com.sluv.server.domain.user.exception;

import com.sluv.server.domain.auth.exception.AuthException;
import org.springframework.http.HttpStatus;

public class NotFoundUserException extends AuthException {
    private static final int ERROR_CODE = 2000;
    private static final String MESSAGE = "존재하지 않는 유저입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public NotFoundUserException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
