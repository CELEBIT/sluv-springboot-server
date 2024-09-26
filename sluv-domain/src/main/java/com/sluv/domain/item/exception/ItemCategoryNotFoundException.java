package com.sluv.domain.item.exception;

import com.sluv.common.exception.HttpStatusCode;

public class ItemCategoryNotFoundException extends ItemException {
    private static final int ERROR_CODE = 2003;
    private static final String MESSAGE = "존재하지 않는 아이템 카테고리입니다.";
    private static final HttpStatusCode STATUS = HttpStatusCode.BAD_REQUEST;

    public ItemCategoryNotFoundException() {
        super(ERROR_CODE, STATUS, MESSAGE);
    }
}
