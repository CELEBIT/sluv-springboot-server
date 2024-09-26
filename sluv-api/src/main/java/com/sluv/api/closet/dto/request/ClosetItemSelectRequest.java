package com.sluv.api.closet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClosetItemSelectRequest {
    @Schema(description = "Closet에서 선택한 Item Id List")
    List<Long> itemList;
}
