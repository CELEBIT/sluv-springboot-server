package com.sluv.domain.item.exception;

import com.sluv.common.exception.HttpStatusCode;

public class ItemNotActiveException extends ItemException {
    private static final int ERROR_CODE = 2025;
    private static final String MESSAGE = "ACTIVE 상태의 Item이 아닙니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public ItemNotActiveException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
