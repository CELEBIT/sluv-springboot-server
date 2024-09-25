package com.sluv.domain.item.exception;

import com.sluv.common.exception.HttpStatusCode;

public class ItemNotFoundException extends ItemException {
    private static final int ERROR_CODE = 2009;
    private static final String MESSAGE = "존재하지 않는 Item입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public ItemNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
