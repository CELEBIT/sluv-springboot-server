package com.sluv.domain.question.exception;

import com.sluv.common.exception.ApplicationException;
import com.sluv.common.exception.HttpStatusCode;

public abstract class QuestionException extends ApplicationException {
    public QuestionException(int errorCode, HttpStatusCode httpStatusCode, String message) {
        super(errorCode, httpStatusCode, message);
    }
}
