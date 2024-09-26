package com.sluv.domain.item.exception;

import com.sluv.common.exception.HttpStatusCode;

public class TempItemNotFoundException extends ItemException {
    private static final int ERROR_CODE = 2008;
    private static final String MESSAGE = "존재하지 않는 Temp Item입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public TempItemNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
