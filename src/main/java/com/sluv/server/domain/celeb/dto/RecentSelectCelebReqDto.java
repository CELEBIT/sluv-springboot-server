package com.sluv.server.domain.celeb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecentSelectCelebReqDto {
    private Long celebId;
    private Long newCelebId;
}
