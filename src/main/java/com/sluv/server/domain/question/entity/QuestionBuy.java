package com.sluv.server.domain.question.entity;

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
    public QuestionBuy(Long id, Long userId, String title, String content, Long searchNum, LocalDateTime voteEndTime) {
        super(id, userId, title, content, searchNum);
        this.voteEndTime = voteEndTime;
    }

}
