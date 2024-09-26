package com.sluv.api.closet.dto.response;

import com.sluv.api.common.response.PaginationResponse;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.enums.ClosetColor;
import com.sluv.domain.closet.enums.ClosetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@SuperBuilder
public class ClosetDetailResponse<T> extends PaginationResponse<T> {
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

    public static <T> ClosetDetailResponse<T> of(Page<T> page, Closet closet) {
        String coverImg = closet.getCoverImgUrl() == null ? null : closet.getCoverImgUrl();
        return ClosetDetailResponse.<T>builder()
                .hasNext(page.hasNext())
                .page(page.getNumber())
                .content(page.getContent())
                .id(closet.getId())
                .coverImgUrl(coverImg)
                .name(closet.getName())
                .closetStatus(closet.getClosetStatus())
                .colorScheme(closet.getColor())
                .itemNum(page.getTotalElements())
                .build();
    }
}
