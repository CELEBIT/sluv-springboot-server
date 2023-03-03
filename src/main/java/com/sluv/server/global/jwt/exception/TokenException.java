package com.sluv.server.global.jwt.exception;

import com.sluv.server.global.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class TokenException extends ApplicationException {
    public TokenException(int errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
