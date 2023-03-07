package com.sluv.server.domain.question.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "question_like")
public class QuestionLike extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_like_id")
    private Long id;

    @NotNull
    private Long questionId;

    @NotNull
    private Long userId;


    @Builder
    public QuestionLike(Long id, Long questionId, Long userId) {
        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
    }
}
