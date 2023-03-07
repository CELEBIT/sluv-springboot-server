package com.sluv.server.domain.question.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class QuestionRecommendCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long questionId;

    @NotNull
    @Size(max = 45)
    private String name;

    @Builder
    public QuestionRecommendCategory(Long id, Long questionId, String name) {
        this.id = id;
        this.questionId = questionId;
        this.name = name;
    }
}
