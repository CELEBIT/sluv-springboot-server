package com.sluv.server.domain.question.entity;

import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
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
public class QuestionRecommendCategory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @NotNull
    @Size(max = 45)
    private String name;

    public static QuestionRecommendCategory toEntity(Question question, String categoryName) {
        return QuestionRecommendCategory.builder()
                .question(question)
                .name(categoryName)
                .build();
    }
}
