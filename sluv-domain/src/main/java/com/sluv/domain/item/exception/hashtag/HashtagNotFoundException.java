package com.sluv.domain.item.exception.hashtag;

import com.sluv.common.exception.HttpStatusCode;

public class HashtagNotFoundException extends HashtagException {
    private static final int ERROR_CODE = 2029;
    private static final String MESSAGE = "존재하지 않는 Hashtag입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public HashtagNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
