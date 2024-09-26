package com.sluv.domain.question.entity;

import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
//@SuperBuilder
@DiscriminatorValue("How")
public class QuestionHowabout extends Question {

    @Builder
    public QuestionHowabout(Long id, User user, String title, String content, Long searchNum,
                            QuestionStatus questionStatus) {
        super(id, user, title, content, searchNum, questionStatus);
    }

    public static QuestionHowabout toEntity(User user, Long questionId, String title, String content) {
        QuestionHowaboutBuilder builder = QuestionHowabout.builder();

        if (questionId != null) {
            builder.id(questionId);
        }

        return builder
                .user(user)
                .title(title)
                .content(content)
                .questionStatus(QuestionStatus.ACTIVE)
                .build();
    }
}
