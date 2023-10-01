package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.entity.ItemLink;
import com.sluv.server.domain.item.entity.TempItemLink;
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

    public static ItemLinkResDto of(ItemLink itemLink){
        return ItemLinkResDto.builder()
                .itemLinkUrl(itemLink.getItemLinkUrl())
                .linkName(itemLink.getLinkName())
                .build();
    }

    public static ItemLinkResDto of(TempItemLink tempItemLink ){
        return ItemLinkResDto.builder()
                .itemLinkUrl(tempItemLink.getTempItemLinkUrl())
                .linkName(tempItemLink.getLinkName())
                .build();
    }

}
