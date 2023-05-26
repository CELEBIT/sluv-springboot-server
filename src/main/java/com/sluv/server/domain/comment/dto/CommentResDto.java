package com.sluv.server.domain.comment.dto;

import com.sluv.server.domain.item.dto.ItemSameResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResDto {
    private Long id;
    private Long userId;
    private String userNickname;
    private String userProfileUrl;
    private String content;
    private List<String> imgUrlList;
    private List<ItemSameResDto> itemList;
    private LocalDateTime createdAt;

    private Integer likeNum;
    private Boolean likeStatus;
    private Boolean hasMine;
    private Long restCommentNum;
}
