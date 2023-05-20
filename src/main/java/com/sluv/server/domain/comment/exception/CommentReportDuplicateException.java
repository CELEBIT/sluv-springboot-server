package com.sluv.server.domain.comment.exception;

import org.springframework.http.HttpStatus;

public class CommentReportDuplicateException extends CommentException {
    private static final int ERROR_CODE = 2016;
    private static final String MESSAGE = "중복된 댓글 신고입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public CommentReportDuplicateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
