package com.sluv.admin.visit.dto;

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
public class VisitHistoryCountResDto {
    @Schema(description = "동일시간 대비 어제와 비교하여 접속자수 증감비율")
    private Double percent;
    @Schema(description = "오늘 누적 접속자 수")
    private Long todayCount;
    @Schema(description = "오늘을 기준으로 10일간 일일 누적 접속자 수 그래프")
    private List<Long> countGraph;

    public static VisitHistoryCountResDto of(Long todayCount, Long yesterdayCount, List<Long> countGraph) {
        return VisitHistoryCountResDto.builder()
                .percent(getPercent(yesterdayCount, todayCount))
                .todayCount(todayCount)
                .countGraph(countGraph)
                .build();
    }

    private static Double getPercent(Long yesterdayCount, Long todayCount) {
        if (todayCount == 0) {
            return 0.0;
        }

        // 계산 후 소숫점 2자리까지 반올림1
        return BigDecimal.valueOf((double) (yesterdayCount - todayCount) / todayCount * 100)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
