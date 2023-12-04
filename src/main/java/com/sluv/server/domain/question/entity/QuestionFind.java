package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.question.dto.QuestionFindPostReqDto;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.domain.user.entity.User;
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

    public static QuestionFind toEntity(User user, QuestionFindPostReqDto postReqDto, Celeb celeb, NewCeleb newCeleb) {
        QuestionFindBuilder builder = QuestionFind.builder();

        if (postReqDto.getId() != null) {
            builder
                    .id(postReqDto.getId());
        }

        return builder
                .user(user)
                .title(postReqDto.getTitle())
                .content(postReqDto.getContent())
                .celeb(celeb)
                .newCeleb(newCeleb)
                .build();
    }
}
