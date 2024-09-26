package com.sluv.domain.item.exception;

import com.sluv.common.exception.ApplicationException;
import com.sluv.common.exception.HttpStatusCode;

public abstract class ItemException extends ApplicationException {
    public ItemException(int errorCode, HttpStatusCode httpStatusCode, String message) {
        super(errorCode, httpStatusCode, message);
    }
}
