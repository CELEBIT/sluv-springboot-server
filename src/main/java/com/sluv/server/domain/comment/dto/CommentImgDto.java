package com.sluv.server.domain.comment.dto;

import com.sluv.server.domain.comment.entity.CommentImg;
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

    public static CommentImgDto of(CommentImg commentImg) {
        return CommentImgDto.builder()
                .imgUrl(commentImg.getImgUrl())
                .sortOrder(commentImg.getSortOrder())
                .build();
    }
}
