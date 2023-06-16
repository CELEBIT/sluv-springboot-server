package com.sluv.server.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentPostReqDto {
    @Schema(description = "Comment 내용")
    private String content;
    @Schema(description = "Comment Img List")
    private List<CommentImgDto> imgList;
    @Schema(description = "Comment Item List")
    private List<CommentItemReqDto> itemList;
}
