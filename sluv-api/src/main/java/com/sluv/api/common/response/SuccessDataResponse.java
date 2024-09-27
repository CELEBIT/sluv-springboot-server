package com.sluv.api.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class SuccessDataResponse<T> extends SuccessResponse {
    private T result;

    @Builder(access = AccessLevel.PRIVATE)
    public SuccessDataResponse(T result) {
        this.result = result;
    }

    public static <T> SuccessDataResponse<T> create(T result) {
        return SuccessDataResponse.<T>builder()
                .result(result)
                .build();
    }

}
