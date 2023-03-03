package com.sluv.server.global.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class SuccessResponse<T> {
    private final Boolean isSuccess = true;
    private final String message = "요청성공.";
    private final int code = 200;
    private T result;

    @Builder
    public SuccessResponse(T result) {
        this.result = result;
    }

}
