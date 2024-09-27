package com.sluv.common.exception;

import lombok.Getter;


@Getter
public abstract class ApplicationException extends RuntimeException {

    private final int errorCode;
    private final HttpStatusCode httpStatusCode;

    protected ApplicationException(int errorCode, HttpStatusCode httpStatusCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatusCode = httpStatusCode;
    }
}
