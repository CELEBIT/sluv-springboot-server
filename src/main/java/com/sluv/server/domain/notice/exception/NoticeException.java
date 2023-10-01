package com.sluv.server.domain.notice.exception;

import com.sluv.server.global.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class NoticeException extends ApplicationException {
    public NoticeException(int errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
