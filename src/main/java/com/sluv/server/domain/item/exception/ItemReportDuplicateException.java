package com.sluv.server.domain.item.exception;

import org.springframework.http.HttpStatus;

public class ItemReportDuplicateException extends ItemException {
    private static final int ERROR_CODE = 2011;
    private static final String MESSAGE = "중복된 아이템 게시글 신고입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public ItemReportDuplicateException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
