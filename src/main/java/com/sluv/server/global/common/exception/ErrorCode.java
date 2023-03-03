package com.sluv.server.global.common.exception;
import lombok.Builder;
import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_ARGUMENT("VALID-001", "Validation을 만족하지 못합니다."),
    INTERNAL_SERVER_ERROR("SERVER-001", "내부 서버 에러입니다."),
    BINDING_EXCEPTION("FORM-400", "적절하지 않은 요청 값입니다."),
    DB_ACCESS_ERROR("DB-001", "DB 에러입니다.");

    private final String code;
    private final String message;
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}