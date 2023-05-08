package com.sluv.server.domain.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemImgResDto {
    @Schema(description = "아이템 이미지 URL")
    private String imgUrl;
    @Schema(description = "대표 이미지 여부(true or false)")
    private Boolean representFlag;

}
