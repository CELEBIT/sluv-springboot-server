package com.sluv.server.domain.comment.entity;

import com.sluv.server.domain.comment.enums.CommentStatus;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @NotNull
    private Long userId;

    private Long parentId;

    @NotNull
    private Long questionId;

    @NotNull
    @Size(max = 1001)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(45) default 'ACTIVE'")
    private CommentStatus commentStatus;

    @Builder
    public Comment(Long id, Long userId, Long questionId, String content, CommentStatus commentStatus) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.content = content;
        this.commentStatus = commentStatus;
    }
}
