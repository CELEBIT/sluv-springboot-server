package com.sluv.domain.user.exception;

import com.sluv.common.exception.ApplicationException;
import com.sluv.common.exception.HttpStatusCode;

public abstract class UserException extends ApplicationException {
    public UserException(int errorCode, HttpStatusCode httpStatusCode, String message) {
        super(errorCode, httpStatusCode, message);
    }
}
