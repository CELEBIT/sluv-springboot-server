package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.question.dto.QuestionHowaboutPostReqDto;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
//@SuperBuilder
@DiscriminatorValue("How")
public class QuestionHowabout extends Question{

    @Builder
    public QuestionHowabout(Long id, User user, String title, String content, Long searchNum, QuestionStatus questionStatus) {
        super(id, user, title, content, searchNum, questionStatus);
    }

    public static QuestionHowabout toEntity(User user, QuestionHowaboutPostReqDto postReqDto){
        QuestionHowaboutBuilder builder = QuestionHowabout.builder();

        if(postReqDto.getId() != null){
            builder
                .id(postReqDto.getId());
        }

        return builder
                .user(user)
                .title(postReqDto.getTitle())
                .content(postReqDto.getContent())
                .build();
    }
}
