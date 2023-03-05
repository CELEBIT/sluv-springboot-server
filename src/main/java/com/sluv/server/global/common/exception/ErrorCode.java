package com.sluv.server.global.common.exception;
import lombok.Builder;
import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_ARGUMENT(3000, "Validation을 만족하지 못합니다."),
    INTERNAL_SERVER_ERROR(5000, "내부 서버 에러입니다."),
    DB_ACCESS_ERROR(5001, "DB 에러입니다.");

    private final int code;
    private final String message;
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}