package com.sluv.infra.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemColorCheckReqDto {
    private String itemImgUrl;

    public static ItemColorCheckReqDto of(String itemImgUrl) {
        return ItemColorCheckReqDto.builder()
                .itemImgUrl(itemImgUrl)
                .build();
    }
}
