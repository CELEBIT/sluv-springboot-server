package com.sluv.server.domain.item.exception;

import org.springframework.http.HttpStatus;

public class ItemNotActiveException extends ItemException {
    private static final int ERROR_CODE = 2025;
    private static final String MESSAGE = "ACTIVE 상태의 Item이 아닙니다.";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public ItemNotActiveException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
