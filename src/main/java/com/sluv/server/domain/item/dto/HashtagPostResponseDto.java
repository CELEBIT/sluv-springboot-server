package com.sluv.server.domain.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class HashtagPostResponseDto {
    @Schema(description = "해쉬태그 id")
    private Long hashtagId;
    @Schema(description = "해쉬태그 내용")
    private String hashtagContent;

    @Builder
    public HashtagPostResponseDto(Long hashtagId, String hashtagContent) {
        this.hashtagId = hashtagId;
        this.hashtagContent = hashtagContent;
    }
}
