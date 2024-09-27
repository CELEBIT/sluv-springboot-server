package com.sluv.domain.question.exception;

import com.sluv.common.exception.HttpStatusCode;

public class QuestionNotFoundException extends QuestionException {
    private static final int ERROR_CODE = 2012;
    private static final String MESSAGE = "존재하지 않는 Question입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public QuestionNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}