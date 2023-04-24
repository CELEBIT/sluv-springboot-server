package com.sluv.server.domain.item.exception;

import com.sluv.server.global.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class ItemException extends ApplicationException {
    public ItemException(int errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
