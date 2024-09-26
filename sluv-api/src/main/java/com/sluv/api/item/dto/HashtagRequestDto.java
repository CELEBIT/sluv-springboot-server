package com.sluv.api.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashtagRequestDto {
    @Schema(description = "새로 등록할 해쉬태그명")
    private String hashtagContent;

}
