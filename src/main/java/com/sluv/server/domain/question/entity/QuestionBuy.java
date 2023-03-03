package com.sluv.server.domain.question.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@SuperBuilder
@DiscriminatorValue("Buy")
public class QuestionBuy extends Question{


    private LocalDateTime voteEndTime;

}
