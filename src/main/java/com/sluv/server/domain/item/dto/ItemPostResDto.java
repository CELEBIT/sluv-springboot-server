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
public class ItemPostResDto {
    @Schema(description = "아이템 Id1")
    private Long itemId;

    public static ItemPostResDto of(Long itemId) {
        return ItemPostResDto.builder()
                .itemId(itemId)
                .build();
    }
}
