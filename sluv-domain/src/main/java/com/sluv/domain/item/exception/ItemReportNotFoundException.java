package com.sluv.domain.item.exception;

import com.sluv.common.exception.HttpStatusCode;

public class ItemReportNotFoundException extends ItemException {

    private static final int ERROR_CODE = 2300;
    private static final String MESSAGE = "존재하지 않는 아이템 게시글 신고입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public ItemReportNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
