package com.sluv.domain.question.exception;

import com.sluv.common.exception.HttpStatusCode;

public class QuestionTypeNotFoundException extends QuestionException {
    private static final int ERROR_CODE = 2020;
    private static final String MESSAGE = "존재하지 않는 Question 타입입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public QuestionTypeNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
