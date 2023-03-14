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
@DiscriminatorValue("How")
public class QuestionHowabout extends Question{

    @Builder
    public QuestionHowabout(Long id, Long userId, String title, String content, Long searchNum) {
        super(id, userId, title, content, searchNum);
    }
}
