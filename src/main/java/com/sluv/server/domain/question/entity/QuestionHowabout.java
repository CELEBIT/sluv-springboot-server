package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.question.enums.QuestionStatus;
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
@DiscriminatorValue("How")
public class QuestionHowabout extends Question{

    @Builder
    public QuestionHowabout(Long id, User user, String title, String content, Long searchNum, QuestionStatus questionStatus) {
        super(id, user, title, content, searchNum, questionStatus);
    }
}
