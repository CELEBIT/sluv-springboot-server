package com.sluv.server.domain.comment.entity;

import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment_item")
public class CommentItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @NotNull
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @Builder
    public CommentItem(Long id, Comment comment, Item item) {
        this.id = id;
        this.comment = comment;
        this.item = item;
    }
}
