package com.sluv.domain.item.dto;

import com.sluv.domain.item.entity.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithCountDto {

    private Long id;
    private String name;
    @Schema(description = "대표 이미지 Url")
    private String imgUrl;
    @Schema(description = "해당 Item의 조회수")
    private Long viewCount;
    @Schema(description = "해당 Item의 좋아요 수")
    private Long likeCount;
    @Schema(description = "해당 Item의 스크랩 수")
    private Long scrapCount;

    public static ItemWithCountDto of(Item item, String imgUrl, Long likeCount, Long scrapCount) {
        return ItemWithCountDto.builder()
                .id(item.getId())
                .name(item.getName())
                .imgUrl(imgUrl)
                .viewCount(item.getViewNum())
                .likeCount(likeCount)
                .scrapCount(scrapCount)
                .build();
    }
}
