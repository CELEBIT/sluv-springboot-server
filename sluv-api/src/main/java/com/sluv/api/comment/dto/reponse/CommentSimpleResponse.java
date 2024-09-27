package com.sluv.api.comment.dto.reponse;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.enums.CommentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentSimpleResponse {
    @Schema(description = "Comment Id")
    private Long id;
    @Schema(description = "Question Id")
    private Long questionId;
    @Schema(description = "Comment가 소속한 Question의 제목")
    private String questionTitle;
    @Schema(description = "Comment 내용")
    private String content;
    @Schema(description = "Comment 상태")
    private CommentStatus commentStatus;

    public static CommentSimpleResponse of(Comment comment) {
        return CommentSimpleResponse.builder()
                .id(comment.getId())
                .questionId(comment.getQuestion().getId())
                .questionTitle(comment.getQuestion().getTitle())
                .content(comment.getContent())
                .commentStatus(comment.getCommentStatus())
                .build();
    }
}
