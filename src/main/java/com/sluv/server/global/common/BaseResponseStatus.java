package com.sluv.server.global.common;
import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    NOT_FOUND_USER(false, 3000, "존재하지 않는 유저입니다."),
    JWT_AUTHENTICATION_FAILED(false, 3001, "유효하지 않은 토큰입니다."),

    JWT_ACCESS_TOKEN_EXPIRED(false, 3002, "ACCESS TOKEN 만료.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}