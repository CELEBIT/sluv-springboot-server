package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.enums.ItemEditReqReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEditReqDto {
    @Schema(description = "수정요청 이유")
    private ItemEditReqReason reason;
    @Schema(description = "수정요청 내용")
    private String content;
}
