package com.sluv.admin.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCountByCategoryResDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserCountByEachCategoryResDto {
        private String category;
        @Schema(description = "카테고리에 해당하는 유저의 수")
        private Long count;
        private Double percent;
    }

    @Schema(description = "각 카테고리의 통계")
    private List<UserCountByEachCategoryResDto> eachCategory;
    @Schema(description = "전체 유저의 수")
    private Long totalCount;

    public static <T> UserCountByCategoryResDto of(HashMap<T, Long> countByCategory, Long totalCount) {
        List<UserCountByEachCategoryResDto> userCountByEachCategories = countByCategory.entrySet().stream()
                .map(entry -> new UserCountByEachCategoryResDto(entry.getKey().toString(), entry.getValue(),
                        getPercent(entry.getValue(), totalCount)))
                .toList();
        return new UserCountByCategoryResDto(userCountByEachCategories, totalCount);
    }

    private static Double getPercent(Long genderCount, Long totalCount) {
        if (totalCount == 0) {
            return 0.0;
        }

        // 계산 후 소숫점 2자리까지 반올림1
        return BigDecimal.valueOf((double) genderCount / totalCount * 100)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
