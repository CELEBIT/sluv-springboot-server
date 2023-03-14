package com.sluv.server.domain.comment.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "comment_like")
public class CommentLike extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @NotNull
    private Long commentId;

    @NotNull
    private Long userId;

    @Builder
    public CommentLike(Long id, Long commentId, Long userId) {
        this.id = id;
        this.commentId = commentId;
        this.userId = userId;
    }
}
