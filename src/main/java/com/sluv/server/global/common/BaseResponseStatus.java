package com.sluv.server.global.common;
import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    JWT_ACCESS_DENIED(false, 3000, "권한이 없는 회원입니다."),
    JWT_AUTHENTICATION_FAILED(false, 3001, "인증에 실패한 회원입니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}