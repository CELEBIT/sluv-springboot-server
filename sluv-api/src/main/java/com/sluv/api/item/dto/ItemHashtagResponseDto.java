package com.sluv.api.item.dto;

import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.entity.hashtag.ItemHashtag;
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
public class ItemHashtagResponseDto implements Serializable {
    @Schema(description = "해쉬태그 id")
    private Long hashtagId;
    @Schema(description = "해쉬태그 내용")
    private String hashtagContent;

    public static ItemHashtagResponseDto from(Hashtag hashtag) {
        return ItemHashtagResponseDto.builder()
                .hashtagId(hashtag.getId())
                .hashtagContent(hashtag.getContent())
                .build();
    }

    public static ItemHashtagResponseDto from(ItemHashtag itemHashtag) {
        return ItemHashtagResponseDto.builder()
                .hashtagId(itemHashtag.getHashtag().getId())
                .hashtagContent(itemHashtag.getHashtag().getContent())
                .build();
    }

}
