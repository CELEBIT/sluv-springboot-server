package com.sluv.server.domain.item.exception.hashtag;

import org.springframework.http.HttpStatus;

public class HashtagNotFoundException extends HashtagException {
    private static final int ERROR_CODE = 2029;
    private static final String MESSAGE = "존재하지 않는 Hashtag입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public HashtagNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
