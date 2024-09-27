package com.sluv.api.closet.dto.request;

import com.sluv.domain.closet.enums.ClosetColor;
import com.sluv.domain.closet.enums.ClosetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClosetRequest {
    @Schema(description = "Closet 이름")
    private String name;
    @Schema(description = "Closet 커버 사진 URL")
    private String coverImgUrl;
    @Schema(description = "Closet 공개 여부")
    private ClosetStatus closetStatus;
    @Schema(description = "Closet 커버 색상")
    private ClosetColor colorScheme;

}
