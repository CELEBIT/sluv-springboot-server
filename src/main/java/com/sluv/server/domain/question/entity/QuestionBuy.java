package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.question.dto.QuestionBuyPostReqDto;
import com.sluv.server.domain.question.enums.QuestionStatus;
import com.sluv.server.domain.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
//@SuperBuilder
@DiscriminatorValue("Buy")
public class QuestionBuy extends Question {


    private LocalDateTime voteEndTime;

    @Builder
    public QuestionBuy(Long id, User user, String title, String content, Long searchNum, QuestionStatus questionStatus,
                       LocalDateTime voteEndTime) {
        super(id, user, title, content, searchNum, questionStatus);
        this.voteEndTime = voteEndTime;
    }

    public static QuestionBuy toEntity(User user, QuestionBuyPostReqDto postReqDto) {
        QuestionBuyBuilder builder = QuestionBuy.builder();

        if (postReqDto.getId() != null) {
            builder
                    .id(postReqDto.getId());
        }

        return builder
                .user(user)
                .title(postReqDto.getTitle())
                .voteEndTime(postReqDto.getVoteEndTime())
                .build();
    }
}
