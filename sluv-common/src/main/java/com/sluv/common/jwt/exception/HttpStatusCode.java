package com.sluv.common.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpStatusCode {

    BAD_REQUEST(400),
    UNAUTHORIZED(401);

    private final int code;


}
