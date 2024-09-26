package com.sluv.api.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceRankReqDto {
    @Schema(description = "발견장소 이름")
    private String placeName;
}
