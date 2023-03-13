package com.sluv.server.global.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class SuccessDataResponse<T> extends SuccessResponse {
    private T result;

    @Builder
    public SuccessDataResponse(T result) {
        this.result = result;
    }

}
