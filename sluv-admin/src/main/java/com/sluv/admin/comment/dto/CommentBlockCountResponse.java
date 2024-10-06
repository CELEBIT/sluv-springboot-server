package com.sluv.admin.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentBlockCountResponse {
    @Schema(description = "오늘 기준으로 한달전 대비 증가 비율")
    private Double percent;
    @Schema(description = "총 Block 댓글 수")
    private Long totalCount;
    @Schema(description = "이번주 기준으로 10주간 가입자 수 그래프")
    private List<Long> countGraph;

    public static CommentBlockCountResponse of(Long totalCount, Long recentMonthCount, List<Long> countGraph) {
        return CommentBlockCountResponse.builder()
                .percent(getPercent(recentMonthCount, totalCount))
                .totalCount(totalCount)
                .countGraph(countGraph)
                .build();
    }

    private static Double getPercent(Long recentCount, Long totalCount) {
        if (totalCount == 0) {
            return 0.0;
        }
        // 계산 후 소숫점 2자리까지 반올림1
        return BigDecimal.valueOf((double) recentCount / totalCount * 100)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
