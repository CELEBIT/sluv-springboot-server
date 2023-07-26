package com.sluv.server.domain.closet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ClosetItemSelectReqDto {
    @Schema(description = "Closet에서 선택한 Item Id List")
    List<Long> itemList;
}
