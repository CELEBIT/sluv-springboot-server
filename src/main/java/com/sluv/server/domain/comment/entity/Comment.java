package com.sluv.server.domain.comment.entity;

import com.sluv.server.domain.comment.enums.CommentReportReason;
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
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @NotNull
    @Size(max = 1001)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(45) default 'ACTIVE'")
    private CommentStatus commentStatus;

    @Builder
    public Comment(Long id, User user, Comment parent, Question question, String content, CommentStatus commentStatus) {
        this.id = id;
        this.user = user;
        this.parent = parent;
        this.question = question;
        this.content = content;
        this.commentStatus = commentStatus;
    }
}
