package com.sluv.domain.user.exception;

import com.sluv.common.exception.HttpStatusCode;

public class UserNicknameDuplicatedException extends UserException {
    private static final int ERROR_CODE = 2017;
    private static final String MESSAGE = "중복된 유저 닉네임입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public UserNicknameDuplicatedException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
