package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.enums.ItemReportReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemReportReqDto {
    private ItemReportReason reportReason;
    private String content;
}
