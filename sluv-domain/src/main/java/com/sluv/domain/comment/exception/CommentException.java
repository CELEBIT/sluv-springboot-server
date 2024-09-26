package com.sluv.domain.comment.exception;

import com.sluv.common.exception.ApplicationException;
import com.sluv.common.exception.HttpStatusCode;

public abstract class CommentException extends ApplicationException {
    public CommentException(int errorCode, HttpStatusCode httpStatusCode, String message) {
        super(errorCode, httpStatusCode, message);
    }
}
