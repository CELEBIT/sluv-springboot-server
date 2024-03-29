package com.sluv.server.global.common.exception;

import lombok.Getter;import org.springframework.http.HttpStatus;


@Getter
public abstract class ApplicationException extends RuntimeException{

    private final int errorCode;
    private final HttpStatus httpStatus;

    protected ApplicationException(int errorCode, HttpStatus httpStatus, String message){
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
