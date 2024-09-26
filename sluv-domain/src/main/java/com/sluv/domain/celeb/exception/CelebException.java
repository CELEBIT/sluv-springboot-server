package com.sluv.domain.celeb.exception;

import com.sluv.common.exception.ApplicationException;
import com.sluv.common.exception.HttpStatusCode;

public abstract class CelebException extends ApplicationException {
    public CelebException(int errorCode, HttpStatusCode httpStatusCode, String message) {
        super(errorCode, httpStatusCode, message);
    }
}
