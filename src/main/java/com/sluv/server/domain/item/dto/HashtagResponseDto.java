package com.sluv.server.domain.item.dto;

import com.sluv.server.domain.item.entity.hashtag.Hashtag;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
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
public class HashtagResponseDto implements Serializable {
    @Schema(description = "해쉬태그 id")
    private Long hashtagId;
    @Schema(description = "해쉬태그 내용")
    private String hashtagContent;
    @Schema(description = "해쉬태그 누적수")
    private Long count;

    public static HashtagResponseDto of(Hashtag hashtag, Long count) {
        return HashtagResponseDto.builder()
                .hashtagId(hashtag.getId())
                .hashtagContent(hashtag.getContent())
                .count(count)
                .build();
    }

}
