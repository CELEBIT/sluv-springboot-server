package com.sluv.api.comment.dto.reponse;

import com.sluv.domain.comment.dto.CommentImgDto;
import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.enums.CommentStatus;
import com.sluv.domain.user.dto.UserInfoDto;
import com.sluv.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    @Schema(description = "Comment Id")
    private Long id;
    @Schema(description = "Comment 작성자 정보")
    private UserInfoDto user;
    @Schema(description = "Comment 내용")
    private String content;
    @Schema(description = "Comment 이미지 Url 리스트")
    private List<CommentImgDto> imgUrlList;
    @Schema(description = "Comment 아이템 리스트")
    private List<CommentItemResponse> itemList;
    @Schema(description = "Comment 작성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "Comment 좋아요 수")
    private Integer likeNum;
    @Schema(description = "현재 유저의 Comment 좋아요 여부 판단")
    private Boolean likeStatus;
    @Schema(description = "현재 유저가 작성한 Comment 인지 판단")
    private Boolean hasMine;
    @Schema(description = "Comment 수정 여부")
    private Boolean modifyStatus;
    @Schema(description = "Comment 상태")
    private CommentStatus commentStatus;


    public static CommentResponse of(Comment comment, User writer, User user,
                                     List<CommentImgDto> imgList,
                                     List<CommentItemResponse> itemList,
                                     Integer likeNum,
                                     Boolean likeStatus) {

        boolean hasMine = user != null && comment.getUser().getId().equals(user.getId());

        return CommentResponse.builder()
                .id(comment.getId())
                .user(UserInfoDto.of(writer))
                .content(comment.getContent())
                .imgUrlList(imgList)
                .itemList(itemList)
                .createdAt(comment.getCreatedAt())
                .likeNum(likeNum)
                .likeStatus(likeStatus)
                .hasMine(hasMine)
                .modifyStatus(!comment.getCreatedAt().equals(comment.getUpdatedAt()))
                .commentStatus(comment.getCommentStatus())
                .build();
    }
}
