package com.sluv.api.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sluv.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message"})
public class ErrorResponse {

    private Boolean isSuccess;
    private int code;
    private String message;

    @Builder
    public ErrorResponse(int code, String message) {
        this.isSuccess = false;
        this.code = code;
        this.message = message;
    }

    @Builder(builderClassName = "customBuilder", builderMethodName = "customBuilder")
    public ErrorResponse(ErrorCode errorCode) {
        this.isSuccess = false;
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

}
