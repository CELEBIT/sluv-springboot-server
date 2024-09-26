package com.sluv.api.celeb.dto.response;

import com.sluv.domain.celeb.entity.CelebActivity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CelebActivityResponse {
    @Schema(description = "셀럽의 활동명")
    private String activityName;

    public static CelebActivityResponse of(CelebActivity celebActivity) {
        return CelebActivityResponse.builder()
                .activityName(celebActivity.getActivityName())
                .build();
    }
}
