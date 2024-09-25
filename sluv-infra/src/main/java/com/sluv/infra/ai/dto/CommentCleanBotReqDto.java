package com.sluv.infra.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCleanBotReqDto {
    private String comment;

    public static CommentCleanBotReqDto of(String comment) {
        return CommentCleanBotReqDto.builder()
                .comment(comment)
                .build();
    }
}
