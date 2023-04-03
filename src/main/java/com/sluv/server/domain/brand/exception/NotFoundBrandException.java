package com.sluv.server.domain.brand.exception;

import org.springframework.http.HttpStatus;

public class NotFoundBrandException extends BrandException {
    private static final int ERROR_CODE = 2002;
    private static final String MESSAGE = "존재하지 않는 셀럽입니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public NotFoundBrandException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
