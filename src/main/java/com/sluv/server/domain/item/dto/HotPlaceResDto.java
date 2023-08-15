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
public class HotPlaceResDto {
    @Schema(description = "장소명")
    private String placeName;

    public static HotPlaceResDto of(String placeName){
        return HotPlaceResDto.builder()
                .placeName(placeName)
                .build();
    }
}
