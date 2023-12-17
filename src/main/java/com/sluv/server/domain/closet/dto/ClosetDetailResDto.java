package com.sluv.server.domain.closet.dto;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.closet.enums.ClosetColor;
import com.sluv.server.domain.closet.enums.ClosetStatus;
import com.sluv.server.global.common.response.PaginationResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@SuperBuilder
public class ClosetDetailResDto<T> extends PaginationResDto<T> {
    @Schema(description = "Closet id")
    private Long id;
    @Schema(description = "Closet 커버 이미지 Url")
    private String coverImgUrl;
    @Schema(description = "Closet 이름")
    private String name;
    @Schema(description = "Closet 공개여부")
    private ClosetStatus closetStatus;
    @Schema(description = "Closet 커버 색상")
    private ClosetColor colorScheme;
    @Schema(description = "Closet에 있는 아이템 개수")
    private Long itemNum;

    public static <T> ClosetDetailResDto<T> of(Page<T> page, Closet closet) {
        return ClosetDetailResDto.<T>builder()
                .hasNext(page.hasNext())
                .page(page.getNumber())
                .content(page.getContent())
                .id(closet.getId())
                .coverImgUrl(closet.getCoverImgUrl())
                .name(closet.getName())
                .closetStatus(closet.getClosetStatus())
                .colorScheme(closet.getColor())
                .itemNum(page.getTotalElements())
                .build();
    }
}
