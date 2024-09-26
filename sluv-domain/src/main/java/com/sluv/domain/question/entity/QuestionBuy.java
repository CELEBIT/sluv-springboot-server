package com.sluv.domain.question.entity;

import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
//@SuperBuilder
@DiscriminatorValue("Buy")
public class QuestionBuy extends Question {


    private LocalDateTime voteEndTime;

    @Builder
    public QuestionBuy(Long id, User user, String title, String content, Long searchNum, QuestionStatus questionStatus,
                       LocalDateTime voteEndTime) {
        super(id, user, title, content, searchNum, questionStatus);
        this.voteEndTime = voteEndTime;
    }

    public static QuestionBuy toEntity(User user, Long questionId, String title, LocalDateTime voteEndTime) {
        QuestionBuyBuilder builder = QuestionBuy.builder();

        if (questionId != null) {
            builder.id(questionId);
        }

        return builder
                .user(user)
                .title(title)
                .voteEndTime(voteEndTime)
                .questionStatus(QuestionStatus.ACTIVE)
                .build();
    }
}
