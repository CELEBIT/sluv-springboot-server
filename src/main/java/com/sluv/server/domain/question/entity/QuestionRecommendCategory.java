package com.sluv.server.domain.question.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class QuestionRecommendCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_question_id")
    @NotNull
    private Question question;

    @NotNull
    @Size(max = 45)
    private String name;

    @Builder
    public QuestionRecommendCategory(Long id, Question question, String name) {
        this.id = id;
        this.question = question;
        this.name = name;
    }
}
