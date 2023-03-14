package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
//@SuperBuilder
@DiscriminatorValue("Find")
public class QuestionFind extends Question{

    private Long celebId;

    @Builder
    public QuestionFind(Long id, User user, String title, String content, Long searchNum, Long celebId) {
        super(id, user, title, content, searchNum);
        this.celebId = celebId;
    }
}
