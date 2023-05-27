package com.sluv.server.domain.user.exception;

import com.sluv.server.domain.auth.exception.AuthException;
import org.springframework.http.HttpStatus;

public class UserNotMatchedException extends UserException {
    private static final int ERROR_CODE = 2015;
    private static final String MESSAGE = "유저 정보가 일치하지 않습니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public UserNotMatchedException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
