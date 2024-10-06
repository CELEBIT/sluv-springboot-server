package com.sluv.admin.item.dto;

import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.item.entity.ItemReport;
import com.sluv.domain.item.enums.ItemReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemReportInfoDto {

    private Long reporterId;
    private String reporterNickname;
    private Long reportedId;
    private String reportedNickname;
    private Long reportId;
    @Schema(description = "아이템 신고 이유(enums)")
    private ItemReportReason reportReason;
    @Schema(description = "신고 상세 내용")
    private String content;
    @Schema(description = "신고 접수 상태")
    private ReportStatus reportStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ItemReportInfoDto from(ItemReport itemReport) {
        return ItemReportInfoDto.builder()
                .reporterId(itemReport.getReporter().getId())
                .reporterNickname(itemReport.getReporter().getNickname())
                .reportedId(itemReport.getItem().getUser().getId())
                .reportedNickname(itemReport.getItem().getUser().getNickname())
                .reportId(itemReport.getId())
                .reportReason(itemReport.getItemReportReason())
                .content(itemReport.getContent())
                .reportStatus(itemReport.getReportStatus())
                .createdAt(itemReport.getCreatedAt())
                .updatedAt(itemReport.getUpdatedAt())
                .build();
    }

}
