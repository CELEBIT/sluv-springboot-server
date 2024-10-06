package com.sluv.admin.comment.dto;

import com.sluv.domain.common.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentReportResDto {

    private ReportStatus reportStatus;

    public static UpdateCommentReportResDto of(ReportStatus reportStatus) {
        return new UpdateCommentReportResDto(
                reportStatus
        );
    }
}
