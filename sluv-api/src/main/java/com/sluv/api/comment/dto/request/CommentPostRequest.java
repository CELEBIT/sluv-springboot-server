package com.sluv.api.comment.dto.request;

import com.sluv.domain.comment.dto.CommentImgDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentPostRequest {
    @Schema(description = "Comment 내용")
    private String content;
    @Schema(description = "Comment Img List")
    private List<CommentImgDto> imgList;
    @Schema(description = "Comment Item List")
    private List<CommentItemRequest> itemList;
}
