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
public class ItemSimpleResDto {
    /**
     * Item 세부 검색 시 하단에 등장하는
     * 연관 Item 정보를 전달
     */
    @Schema(description = "아이템 Id")
    private Long itemId;
    @Schema(description = "아이템의 대표 사진 Url")
    private String imgUrl;
    @Schema(description = "아이템의 브랜드 이름")
    private String brandName;
    @Schema(description = "아이템의 이름")
    private String itemName;
    @Schema(description = "아이템의 셀럽 이름")
    private String celebName;
    @Schema(description = "스크랩 여부")
    private Boolean scrapStatus;
}
