package com.sluv.domain.comment.entity;

import com.sluv.domain.comment.enums.CommentStatus;
import com.sluv.domain.common.entity.BaseEntity;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @NotNull
    @Size(max = 1001)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(45) default 'ACTIVE'")
    private CommentStatus commentStatus;

    public void changeContent(String content) {
        this.content = content;
    }

    public static Comment toEntity(User user, Question question, String content) {
        return Comment.builder()
                .user(user)
                .question(question)
                .content(content)
                .commentStatus(CommentStatus.ACTIVE)
                .build();
    }

    public static Comment toEntity(User user, Question question, String content, Comment parent) {
        return Comment.builder()
                .user(user)
                .question(question)
                .content(content)
                .parent(parent)
                .commentStatus(CommentStatus.ACTIVE)
                .build();
    }

    public void changeStatus(CommentStatus commentStatus) {
        this.commentStatus = commentStatus;
    }
}
