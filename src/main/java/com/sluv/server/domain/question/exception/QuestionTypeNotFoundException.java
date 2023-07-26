package com.sluv.server.domain.question.exception;

import org.springframework.http.HttpStatus;

public class QuestionTypeNotFoundException extends QuestionException {
    private static final int ERROR_CODE = 2020;
    private static final String MESSAGE = "존재하지 않는 Question 타입입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public QuestionTypeNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
