package com.sluv.server.domain.comment.entity;

import com.sluv.server.domain.comment.enums.CommentStatus;
import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_comment_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "question_question_id")
    @NotNull
    private Question question;

    @NotNull
    @Size(max = 1001)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(45) default 'ACTIVE'")
    private CommentStatus commentStatus;

    @OneToMany(mappedBy = "parentComment")
    List<Comment> commentList;

    @OneToMany(mappedBy = "comment")
    List<CommentImg> commentImgList;

    @Builder
    public Comment(Long id, User user, Question question, String content, CommentStatus commentStatus) {
        this.id = id;
        this.user = user;
        this.question = question;
        this.content = content;
        this.commentStatus = commentStatus;
    }
}
