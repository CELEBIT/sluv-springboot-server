package com.sluv.api.comment.dto.reponse;

import com.sluv.domain.comment.entity.CommentItem;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.ItemImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentItemResponse {
    @Schema(description = "Comment 아이템 정보")
    private ItemSimpleDto item;
    @Schema(description = "Comment 아이템 순서")
    private Integer sortOrder;

    public static CommentItemResponse of(CommentItem commentItem, ItemImg mainImg, Boolean scrapStatus) {

        ItemSimpleDto itemSimpleDto = ItemSimpleDto.of(commentItem.getItem(), mainImg, scrapStatus);

        return CommentItemResponse.builder()
                .item(itemSimpleDto)
                .sortOrder(commentItem.getSortOrder())
                .build();
    }
}
