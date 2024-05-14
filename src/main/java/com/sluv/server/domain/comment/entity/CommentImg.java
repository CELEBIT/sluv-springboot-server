package com.sluv.server.domain.comment.entity;

import com.sluv.server.domain.comment.dto.CommentImgDto;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment_img")
public class CommentImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @NotNull
    private Comment comment;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    private Integer sortOrder;

    public static CommentImg toEntity(Comment comment, CommentImgDto dto) {
        return CommentImg.builder()
                .comment(comment)
                .imgUrl(dto.getImgUrl())
                .sortOrder(dto.getSortOrder())
                .build();
    }
}
