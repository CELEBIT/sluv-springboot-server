package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.question.dto.QuestionRecommendPostReqDto;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("Recommend")
public class QuestionRecommend extends Question{
    @Builder
    public QuestionRecommend(Long id, User user, String title, String content, Long searchNum, QuestionStatus questionStatus) {
        super(id, user, title, content, searchNum, questionStatus);
    }

    public static QuestionRecommend toEntity(User user, QuestionRecommendPostReqDto postReqDto){
        QuestionRecommendBuilder builder = QuestionRecommend.builder();

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
