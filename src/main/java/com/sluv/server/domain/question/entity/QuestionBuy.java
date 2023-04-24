package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
//@SuperBuilder
@DiscriminatorValue("Buy")
public class QuestionBuy extends Question{


    private LocalDateTime voteEndTime;

    @Builder
    public QuestionBuy(Long id, User user, String title, String content, Long searchNum, LocalDateTime voteEndTime) {
        super(id, user, title, content, searchNum);
        this.voteEndTime = voteEndTime;
    }

}
