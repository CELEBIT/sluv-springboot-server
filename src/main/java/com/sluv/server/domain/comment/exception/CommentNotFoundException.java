package com.sluv.server.domain.comment.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends CommentException {
    private static final int ERROR_CODE = 2014;
    private static final String MESSAGE = "존재하지 않는 Comment입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public CommentNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
