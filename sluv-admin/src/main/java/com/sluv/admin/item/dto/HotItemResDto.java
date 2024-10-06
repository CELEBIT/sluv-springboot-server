package com.sluv.admin.item.dto;

import com.sluv.domain.item.dto.ItemWithCountDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotItemResDto {
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

    public static HotItemResDto from(ItemWithCountDto countDto) {
        return HotItemResDto.builder()
                .id(countDto.getId())
                .name(countDto.getName())
                .imgUrl(countDto.getImgUrl())
                .viewCount(countDto.getViewCount())
                .likeCount(countDto.getLikeCount())
                .scrapCount(countDto.getScrapCount())
                .build();
    }
}
