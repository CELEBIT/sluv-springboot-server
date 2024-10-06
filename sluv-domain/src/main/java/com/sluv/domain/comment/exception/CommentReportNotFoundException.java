package com.sluv.domain.comment.exception;

import com.sluv.common.exception.HttpStatusCode;

public class CommentReportNotFoundException extends CommentException {

    private static final int ERROR_CODE = 2200;
    private static final String MESSAGE = "존재하지 않는 댓글 신고입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public CommentReportNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
