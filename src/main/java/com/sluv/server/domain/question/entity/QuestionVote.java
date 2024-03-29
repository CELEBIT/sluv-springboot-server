package com.sluv.server.domain.question.entity;

import com.sluv.server.domain.question.dto.QuestionVoteReqDto;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "question_vote")
public class QuestionVote extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_vote_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long voteSortOrder;

    public static QuestionVote toEntity(Question question, User user, QuestionVoteReqDto dto){
        return QuestionVote.builder()
                .question(question)
                .user(user)
                .voteSortOrder(dto.getVoteSortOrder())
                .build();
    }

}
