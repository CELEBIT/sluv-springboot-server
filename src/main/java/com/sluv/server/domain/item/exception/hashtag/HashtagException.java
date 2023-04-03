package com.sluv.server.domain.item.exception.hashtag;

import com.sluv.server.global.common.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class HashtagException extends ApplicationException {
    public HashtagException(int errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
