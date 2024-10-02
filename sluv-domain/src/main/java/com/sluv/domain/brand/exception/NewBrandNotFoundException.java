package com.sluv.domain.brand.exception;

import com.sluv.common.exception.HttpStatusCode;

public class NewBrandNotFoundException extends BrandException {
    private static final int ERROR_CODE = 2007;
    private static final String MESSAGE = "존재하지 않는 New 브랜드입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public NewBrandNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}