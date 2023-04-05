package com.sluv.server.domain.item.exception.hashtag;

import org.springframework.http.HttpStatus;

public class NotFoundHashtagException extends HashtagException {
    private static final int ERROR_CODE = 2003;
    private static final String MESSAGE = "존재하지 않는 아이템 카테고리입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public NotFoundHashtagException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
