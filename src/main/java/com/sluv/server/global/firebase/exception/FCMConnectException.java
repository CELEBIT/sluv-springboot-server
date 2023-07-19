package com.sluv.server.global.firebase.exception;

import com.sluv.server.global.jwt.exception.TokenException;
import org.springframework.http.HttpStatus;

public class FCMConnectException extends TokenException {

    private static final int ERROR_CODE = 2022;
    private static final String MESSAGE = "Firebase connection 에러입니.";
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    public FCMConnectException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
