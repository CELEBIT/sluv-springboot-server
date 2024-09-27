package com.sluv.domain.comment.exception;

import com.sluv.common.exception.HttpStatusCode;

public class CommentReportDuplicateException extends CommentException {
    private static final int ERROR_CODE = 2016;
    private static final String MESSAGE = "중복된 댓글 신고입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public CommentReportDuplicateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
