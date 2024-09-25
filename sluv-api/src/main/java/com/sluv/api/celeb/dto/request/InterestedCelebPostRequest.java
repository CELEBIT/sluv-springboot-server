package com.sluv.api.celeb.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedCelebPostRequest {
    @Schema(description = "유저가 선택한 관심셀럽의 Id 리스트")
    private List<Long> celebIdList;
}
