package com.sluv.domain.question.entity;

import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("Recommend")
public class QuestionRecommend extends Question {
    @Builder
    public QuestionRecommend(Long id, User user, String title, String content, Long searchNum,
                             QuestionStatus questionStatus) {
        super(id, user, title, content, searchNum, questionStatus);
    }

    public static QuestionRecommend toEntity(User user, Long questionId, String title, String content) {
        QuestionRecommendBuilder builder = QuestionRecommend.builder();

        if (questionId != null) {
            builder
                    .id(questionId);
        }

        return builder
                .user(user)
                .title(title)
                .content(content)
                .questionStatus(QuestionStatus.ACTIVE)
                .build();
    }
}
