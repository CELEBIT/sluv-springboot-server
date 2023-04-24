package com.sluv.server.domain.brand.exception;

import com.sluv.server.global.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class BrandException extends ApplicationException {
    public BrandException(int errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
