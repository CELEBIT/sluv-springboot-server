package com.sluv.server.domain.search.exception;

import com.sluv.server.domain.question.exception.QuestionException;
import org.springframework.http.HttpStatus;

public class ElasticSearchConnectionException extends QuestionException {
    private static final int ERROR_CODE = 2021;
    private static final String MESSAGE = "ElasticSearch Connection 에러입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public ElasticSearchConnectionException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
