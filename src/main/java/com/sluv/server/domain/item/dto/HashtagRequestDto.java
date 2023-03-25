package com.sluv.server.domain.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HashtagRequestDto {
    private String hashtagContent;

    @Builder
    public HashtagRequestDto(String hashtagContent) {
        this.hashtagContent = hashtagContent;
    }
}
