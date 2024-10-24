package com.sluv.admin.celeb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewCelebChangeRequest {
    private Long newCelebId;
    private Long celebId;
}
