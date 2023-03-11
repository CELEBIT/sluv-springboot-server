package com.sluv.server.domain.comment.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "comment_item")
public class CommentItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_item_id")
    private Long id;

    @NotNull
    private Long commentId;

    @NotNull
    private Long itemId;

    @Builder
    public CommentItem(Long id, Long commentId, Long itemId) {
        this.id = id;
        this.commentId = commentId;
        this.itemId = itemId;
    }
}
