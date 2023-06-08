package com.sluv.server.domain.closet.dto;

import com.sluv.server.domain.closet.enums.ClosetStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class
ClosetReqDto {
    @Schema(description = "Closet 이름")
    private String name;
    @Schema(description = "Closet 커버 사진 URL")
    private String coverImgUrl;
    @Schema(description = "Closet 공개 여부")
    private ClosetStatus closetStatus;
    @Schema(description = "Closet 커버 색상")
    private String color;

}
