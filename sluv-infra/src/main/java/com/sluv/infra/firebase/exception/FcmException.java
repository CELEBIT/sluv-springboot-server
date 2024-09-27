package com.sluv.infra.firebase.exception;

import com.sluv.common.exception.ApplicationException;
import com.sluv.common.exception.HttpStatusCode;

public abstract class FcmException extends ApplicationException {
    public FcmException(int errorCode, HttpStatusCode httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}