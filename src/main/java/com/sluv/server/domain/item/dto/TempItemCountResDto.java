package com.sluv.server.domain.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempItemCountResDto {

    @Schema(description = "tempItem의 개수")
    private Long tempItemCount;

    public static TempItemCountResDto of(Long count) {
        return TempItemCountResDto.builder()
                .tempItemCount(count)
                .build();
    }
}
