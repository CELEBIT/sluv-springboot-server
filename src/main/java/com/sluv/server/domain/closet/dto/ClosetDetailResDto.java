package com.sluv.server.domain.closet.dto;

import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.global.common.response.PaginationResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class ClosetDetailResDto<T> extends PaginationResDto<T> {
    @Schema(description = "Closet 커버 이미지 Url")
    private String coverImgUrl;
    @Schema(description = "Closet 이름")
    private String name;
    @Schema(description = "Closet 공개여부")
    private ClosetStatus closetStatus;
    @Schema(description = "Closet에 있는 아이템 개수")
    private Long itemNum;

//    public ClosetDetailResDto(Boolean hasNext, Integer page, List<T> content, String coverImgUrl, String title, ClosetStatus closetStatus, Long itemNum) {
//        super(hasNext, page, content);
//        this.coverImgUrl = coverImgUrl;
//        this.title = title;
//        this.closetStatus = closetStatus;
//        this.itemNum = itemNum;
//    }
}
