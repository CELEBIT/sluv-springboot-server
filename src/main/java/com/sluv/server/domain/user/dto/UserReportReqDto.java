package com.sluv.server.domain.user.dto;

import com.sluv.server.domain.user.enums.UserReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReportReqDto {
    @Schema(description = "신고 이유")
    private UserReportReason reportReason;
    @Schema(description = "신고 내용")
    private String content;
}
