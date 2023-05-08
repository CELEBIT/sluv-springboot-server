package com.sluv.server.domain.celeb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentSelectCelebReqDto {
    @Schema(description = "셀럽의 Id")
    private Long celebId;
    @Schema(description = "뉴셀럽의 Id")
    private Long newCelebId;
}
