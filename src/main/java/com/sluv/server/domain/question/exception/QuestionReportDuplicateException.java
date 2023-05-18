package com.sluv.server.domain.question.exception;

import com.sluv.server.domain.item.exception.ItemException;
import org.springframework.http.HttpStatus;

public class QuestionReportDuplicateException extends QuestionException {
    private static final int ERROR_CODE = 2013;
    private static final String MESSAGE = "중복된 Question 게시글 신고입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public QuestionReportDuplicateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
