package com.sluv.api.item.dto;

import com.sluv.domain.item.entity.PlaceRank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceRankResDto {
    @Schema(description = "입력한 장소 이름")
    private String placeName;

    public static PlaceRankResDto of(PlaceRank placeRank) {
        return PlaceRankResDto.builder()
                .placeName(placeRank.getPlace())
                .build();
    }
}
