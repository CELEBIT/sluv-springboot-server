package com.sluv.server.domain.celeb.dto;

import com.sluv.server.domain.celeb.entity.CelebActivity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CelebActivityResDto {
    @Schema(description = "셀럽의 활동명")
    private String activityName;

    public static CelebActivityResDto of(CelebActivity celebActivity){
        return CelebActivityResDto.builder()
                .activityName(celebActivity.getActivityName())
                .build();
    }
}
