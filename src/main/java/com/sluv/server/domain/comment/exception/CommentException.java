package com.sluv.server.domain.comment.exception;

import com.sluv.server.global.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class CommentException extends ApplicationException {
    public CommentException(int errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
