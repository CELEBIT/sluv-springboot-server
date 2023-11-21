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
public class TempItemPostResDto {
    @Schema(description = "TempItem Id")
    private Long tempItemId;

    public static TempItemPostResDto of(Long tempItemId) {
        return TempItemPostResDto.builder()
                .tempItemId(tempItemId)
                .build();
    }
}
