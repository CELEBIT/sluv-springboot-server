package com.sluv.server.global.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class SuccessResponse<T> {
    private final Boolean isSuccess = true;

    @Schema(defaultValue = "요청성공.")
    private final String message = "요청성공.";

    @Schema(defaultValue = "1000")
    private final int code = 1000;
    private T result;

    @Builder
    public SuccessResponse(T result) {
        this.result = result;
    }
}
