package com.sluv.server.domain.comment.dto;

import com.sluv.server.domain.item.dto.ItemSameResDto;
import com.sluv.server.domain.user.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Comment Id")
    private Long id;
    @Schema(description = "Comment 작성자 정보")
    private UserInfoDto user;
    @Schema(description = "Comment 내용")
    private String content;
    @Schema(description = "Comment 이미지 Url 리스트")
    private List<String> imgUrlList;
    @Schema(description = "Comment 아이템 리스트")
    private List<ItemSameResDto> itemList;
    @Schema(description = "Comment 작성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "Comment 좋아요 수")
    private Integer likeNum;
    @Schema(description = "현재 유저의 Comment 좋아요 여부 판단")
    private Boolean likeStatus;
    @Schema(description = "현재 유저가 작성한 Comment 인지 판단")
    private Boolean hasMine;
    @Schema(description = "현재 Comment의 대댓글 중 로딩 되지 않은 수")
    private Long restCommentNum;
}
