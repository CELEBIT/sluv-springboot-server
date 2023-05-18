package com.sluv.server.domain.question.exception;

import com.sluv.server.domain.item.exception.ItemException;
import org.springframework.http.HttpStatus;

public class QuestionNotFoundException extends ItemException {
    private static final int ERROR_CODE = 2012;
    private static final String MESSAGE = "존재하지 않는 Question입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public QuestionNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
