package com.sluv.server.domain.comment.dto;

import com.sluv.server.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentSimpleResDto {
    private Long id;
    private String questionTitle;
    private String content;

    public static CommentSimpleResDto of(Comment comment){
        return CommentSimpleResDto.builder()
                .id(comment.getId())
                .questionTitle(comment.getQuestion().getTitle())
                .content(comment.getContent())
                .build();
    }
}
