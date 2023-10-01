package com.sluv.server.domain.question.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionVoteReqDto {
    private Long voteSortOrder;
}
