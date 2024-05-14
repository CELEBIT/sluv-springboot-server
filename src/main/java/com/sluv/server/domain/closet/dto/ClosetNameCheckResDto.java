package com.sluv.server.domain.closet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClosetNameCheckResDto {
    @Schema(description = "Closet의 이름이 중복인지 확인")
    Boolean isDuplicated;

    public static ClosetNameCheckResDto of(Boolean checkStatus) {
        return ClosetNameCheckResDto.builder()
                .isDuplicated(checkStatus)
                .build();
    }
}
