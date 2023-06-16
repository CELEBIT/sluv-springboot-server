package com.sluv.server.domain.comment.dto;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentItemResDto {
    @Schema(description = "Comment 아이템 정보")
    private ItemSimpleResDto item;
    @Schema(description = "Comment 아이템 순서")
    private Integer order;
}
