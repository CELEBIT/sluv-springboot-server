package com.sluv.server.domain.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemLinkResDto {

    @Schema(description = "아이템 링크 URL")
    private String itemLinkUrl;
    @Schema(description = "아이템 링크 이름")
    private String linkName;

}
