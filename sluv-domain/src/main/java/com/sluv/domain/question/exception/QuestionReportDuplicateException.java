package com.sluv.domain.question.exception;

import com.sluv.common.exception.HttpStatusCode;

public class QuestionReportDuplicateException extends QuestionException {
    private static final int ERROR_CODE = 2013;
    private static final String MESSAGE = "중복된 Question 게시글 신고입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public QuestionReportDuplicateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
