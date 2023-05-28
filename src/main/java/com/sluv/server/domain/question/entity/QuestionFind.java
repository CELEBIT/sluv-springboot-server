package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "celeb_id")
    private Celeb celeb;

    @ManyToOne
    @JoinColumn(name = "new_celeb_id")
    private NewCeleb newCeleb;

    @Builder
    public QuestionFind(Long id, User user, String title, String content, Long searchNum, Celeb celeb, NewCeleb newCeleb, QuestionStatus questionStatus) {
        super(id, user, title, content, searchNum, questionStatus);
        this.celeb = celeb;
        this.newCeleb = newCeleb;
    }
}
