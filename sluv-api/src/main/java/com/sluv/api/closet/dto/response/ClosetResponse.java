package com.sluv.api.closet.dto.response;

import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.closet.enums.ClosetColor;
import com.sluv.domain.closet.enums.ClosetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClosetResponse {
    @Schema(description = "Closet id")
    private Long id;
    @Schema(description = "Closet 이름")
    private String name;
    @Schema(description = "Closet 커버 사진 URL")
    private String coverImgUrl;
    @Schema(description = "Closet 공개 여부")
    private ClosetStatus closetStatus;
    @Schema(description = "Closet 커버 색상")
    private ClosetColor colorScheme;
    @Schema(description = "해당 Closet의 Item 개수")
    private Long itemNum;

    public static ClosetResponse of(Closet closet, Long itemNum) {
        String coverImg = closet.getCoverImgUrl() == null ? null : closet.getCoverImgUrl();
        return ClosetResponse.builder()
                .id(closet.getId())
                .name(closet.getName())
                .coverImgUrl(coverImg)
                .closetStatus(closet.getClosetStatus())
                .colorScheme(closet.getColor())
                .itemNum(itemNum)
                .build();
    }

}