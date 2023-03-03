package com.sluv.server.global.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sluv.server.global.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message"})
public class ErrorResponse {

    private Boolean isSuccess;
    private String code;
    private String message;

    @Builder
    public ErrorResponse(String code, String message) {
        this.isSuccess = false;
        this.code = code;
        this.message = message;
    }

    @Builder(builderMethodName = "customBuilder")
    public ErrorResponse(ErrorCode errorCode) {
        this.isSuccess = false;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
