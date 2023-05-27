package com.sluv.server.domain.user.exception;

import org.springframework.http.HttpStatus;

public class UserNicknameDuplicatedException extends UserException {
    private static final int ERROR_CODE = 2017;
    private static final String MESSAGE = "중복된 유저 닉네임입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public UserNicknameDuplicatedException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
