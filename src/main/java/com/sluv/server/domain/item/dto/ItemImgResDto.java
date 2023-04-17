package com.sluv.server.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemImgResDto {

    private String imgUrl;
    private Boolean representFlag;

}
