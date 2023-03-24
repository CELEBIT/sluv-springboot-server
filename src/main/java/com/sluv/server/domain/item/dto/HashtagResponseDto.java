package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class HashtagResponseDto {
    private Long hashtagId;
    private String hashtagContent;
    private Long count;

    @Builder
    public HashtagResponseDto(Long hashtagId, String hashtagContent, Long count) {
        this.hashtagId = hashtagId;
        this.hashtagContent = hashtagContent;
        this.count = count;
    }
}
