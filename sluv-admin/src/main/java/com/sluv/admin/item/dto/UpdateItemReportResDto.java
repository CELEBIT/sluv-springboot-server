package com.sluv.admin.item.dto;

import com.sluv.domain.common.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemReportResDto {

    private ReportStatus reportStatus;

    public static UpdateItemReportResDto of(ReportStatus reportStatus) {
        return new UpdateItemReportResDto(
                reportStatus
        );
    }
}
