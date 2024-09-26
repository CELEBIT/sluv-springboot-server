package com.sluv.domain.question.entity;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.question.enums.QuestionStatus;
import com.sluv.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
//@SuperBuilder
@DiscriminatorValue("Find")
public class QuestionFind extends Question {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celeb_id")
    private Celeb celeb;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_celeb_id")
    private NewCeleb newCeleb;

    @Builder
    public QuestionFind(Long id, User user, String title, String content, Long searchNum, Celeb celeb,
                        NewCeleb newCeleb, QuestionStatus questionStatus) {
        super(id, user, title, content, searchNum, questionStatus);
        this.celeb = celeb;
        this.newCeleb = newCeleb;
    }

    public static QuestionFind toEntity(User user, Long questionId, String title, String content, Celeb celeb,
                                        NewCeleb newCeleb) {
        QuestionFindBuilder builder = QuestionFind.builder();

        if (questionId != null) {
            builder
                    .id(questionId);
        }

        return builder
                .user(user)
                .title(title)
                .content(content)
                .celeb(celeb)
                .newCeleb(newCeleb)
                .questionStatus(QuestionStatus.ACTIVE)
                .build();
    }
}
