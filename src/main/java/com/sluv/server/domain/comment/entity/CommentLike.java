package com.sluv.server.domain.comment.entity;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comment_like")
public class CommentLike extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @NotNull
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public static CommentLike toEntity(User user, Comment comment){
        return CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();
    }
}
