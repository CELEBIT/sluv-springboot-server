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
public class CommentItemReqDto {
    @Schema(description = "Comment 아이템 아이디")
    private Long itemId;
    @Schema(description = "Comment 아이템 순서")
    private Integer sortOrder;
}
