package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestedCelebPostReqDto {
    @Schema(description = "유저가 선택한 관심셀럽의 Id 리스트")
    private List<Long> celebIdList;
}
