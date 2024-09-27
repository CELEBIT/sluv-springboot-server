package com.sluv.domain.item.dto;

import com.sluv.domain.item.entity.ItemLink;
import com.sluv.domain.item.entity.TempItemLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemLinkDto implements Serializable {

    @Schema(description = "아이템 링크 URL")
    private String itemLinkUrl;
    @Schema(description = "아이템 링크 이름")
    private String linkName;

    public static ItemLinkDto of(ItemLink itemLink) {
        return ItemLinkDto.builder()
                .itemLinkUrl(itemLink.getItemLinkUrl())
                .linkName(itemLink.getLinkName())
                .build();
    }

    public static ItemLinkDto of(TempItemLink tempItemLink) {
        return ItemLinkDto.builder()
                .itemLinkUrl(tempItemLink.getTempItemLinkUrl())
                .linkName(tempItemLink.getLinkName())
                .build();
    }

}
