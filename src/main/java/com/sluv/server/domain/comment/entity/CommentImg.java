package com.sluv.server.domain.comment.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "comment_img")
public class CommentImg extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_img_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @NotNull
    private Comment comment;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    private Integer order;

    @Builder
    public CommentImg(Long id, Comment comment, String imgUrl, Integer order) {
        this.id = id;
        this.comment = comment;
        this.imgUrl = imgUrl;
        this.order = order;
    }
}
