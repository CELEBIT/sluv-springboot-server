package com.sluv.api.closet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClosetNameCheckResponse {
    @Schema(description = "Closet의 이름이 중복인지 확인")
    Boolean isDuplicated;

    public static ClosetNameCheckResponse of(Boolean checkStatus) {
        return ClosetNameCheckResponse.builder()
                .isDuplicated(checkStatus)
                .build();
    }

}
