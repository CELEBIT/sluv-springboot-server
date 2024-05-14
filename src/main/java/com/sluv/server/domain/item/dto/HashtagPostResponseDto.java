package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashtagPostResponseDto {
    @Schema(description = "해쉬태그 id")
    private Long hashtagId;
    @Schema(description = "해쉬태그 내용")
    private String hashtagContent;

    public static HashtagPostResponseDto of(Hashtag hashtag) {
        return HashtagPostResponseDto.builder()
                .hashtagId(hashtag.getId())
                .hashtagContent(hashtag.getContent())
                .build();
    }
}
