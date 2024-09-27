package com.sluv.api.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message"})
public class SuccessResponse {
    private final Boolean isSuccess = true;

    @Schema(defaultValue = "요청성공.")
    private final String message = "요청성공.";

    @Schema(defaultValue = "1000")
    private final int code = 1000;

    public static SuccessResponse create() {
        return new SuccessResponse();
    }

}
