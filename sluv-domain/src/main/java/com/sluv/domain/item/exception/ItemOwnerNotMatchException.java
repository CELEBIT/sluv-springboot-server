package com.sluv.domain.item.exception;

import com.sluv.common.exception.HttpStatusCode;

public class ItemOwnerNotMatchException extends ItemException {

    private static final int ERROR_CODE = 2030;
    private static final String MESSAGE = "아이템 작성자가 일치하지 않습니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public ItemOwnerNotMatchException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }

}
