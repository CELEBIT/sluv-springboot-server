package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "question_like")
public class QuestionLike extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;


    public static QuestionLike toEntity(User user, Question question) {
        return QuestionLike.builder()
                .user(user)
                .question(question)
                .build();
    }
}
