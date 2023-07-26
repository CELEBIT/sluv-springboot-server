package com.sluv.server.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentImgDto {
    @Schema(description = "Comment 이미지 Url")
    private String imgUrl;
    @Schema(description = "Comment 이미지 순서")
    private Integer sortOrder;
}
