package com.sluv.domain.comment.exception;

import com.sluv.common.exception.HttpStatusCode;

public class CommentNotFoundException extends CommentException {
    private static final int ERROR_CODE = 2014;
    private static final String MESSAGE = "존재하지 않는 Comment입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public CommentNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
