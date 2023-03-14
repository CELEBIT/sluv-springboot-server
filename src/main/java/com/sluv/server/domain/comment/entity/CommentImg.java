package com.sluv.server.domain.comment.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import com.sluv.server.global.common.enums.ImgStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Builder
    public CommentImg(Long id, Comment comment, String imgUrl) {
        this.id = id;
        this.comment = comment;
        this.imgUrl = imgUrl;
    }
}
