package com.sluv.server.domain.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HashtagRequestDto {
    @Schema(description = "새로 등록할 해쉬태그명")
    private String hashtagContent;

    @Builder
    public HashtagRequestDto(String hashtagContent) {
        this.hashtagContent = hashtagContent;
    }
}
