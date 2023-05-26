package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.enums.ItemReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemReportReqDto {
    @Schema(description = "신고 이유")
    private ItemReportReason reason;
    @Schema(description = "신고 내용")
    private String content;
}
