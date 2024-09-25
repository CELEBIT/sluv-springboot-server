package com.sluv.infra.firebase.exception;

import com.sluv.common.exception.HttpStatusCode;

public class FcmConnectException extends FcmException {

    private static final int ERROR_CODE = 2022;
    private static final String MESSAGE = "Firebase connection 에러입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.UNAUTHORIZED;

    public FcmConnectException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}