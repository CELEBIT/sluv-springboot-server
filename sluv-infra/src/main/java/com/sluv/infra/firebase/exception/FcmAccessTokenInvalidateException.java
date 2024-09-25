package com.sluv.infra.firebase.exception;

import com.sluv.common.exception.HttpStatusCode;

public class FcmAccessTokenInvalidateException extends FcmException {

    private static final int ERROR_CODE = 2028;
    private static final String MESSAGE = "잘못된 FCM 토큰입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.UNAUTHORIZED;

    public FcmAccessTokenInvalidateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}