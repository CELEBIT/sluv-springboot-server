package com.sluv.api.question.service;

import com.sluv.api.question.dto.QuestionVoteDataDto;
import com.sluv.api.question.dto.QuestionImgResDto;
import com.sluv.api.question.dto.QuestionItemResDto;
import com.sluv.api.question.dto.QuestionVoteReqDto;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.question.entity.QuestionVote;
import com.sluv.domain.question.service.QuestionDomainService;
import com.sluv.domain.question.service.QuestionVoteDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionVoteService {

    private final QuestionDomainService questionDomainService;
    private final QuestionVoteDomainService questionVoteDomainService;
    private final UserDomainService userDomainService;

    @Transactional
    public void postQuestionVote(Long userId, Long questionId, QuestionVoteReqDto dto) {
        User user = userDomainService.findById(userId);
        log.info("질문 게시글 투표 - 사용자 : {}, 질문 게시글 : {}, 투표 : {}", user.getId(), questionId, dto.getVoteSortOrder());
        QuestionVote questionVote = questionVoteDomainService.findByQuestionIdAndUserIdOrNull(questionId, user.getId());

        if (questionVote == null) {
            Question question = questionDomainService.findById(questionId);
            questionVoteDomainService.saveQuestionVote(question, user, dto.getVoteSortOrder());
        } else {
            questionVoteDomainService.deleteById(questionVote.getId());
        }
    }

    @Transactional(readOnly = true)
    public QuestionVoteDataDto getVoteData(Long questionId, Long sortOrder) {
        List<QuestionVote> questionVotes = questionVoteDomainService.findAllByQuestionId(questionId);

        long voteNum = questionVotes.stream()
                .filter(questionVote -> Objects.equals(questionVote.getVoteSortOrder(), sortOrder))
                .count();

        return QuestionVoteDataDto.of(
                voteNum,
                questionVotes.isEmpty() ? 0.0 : getVotePercent(voteNum, (long) questionVotes.size())
        );
    }

    public Long getTotalVoteCount(List<QuestionImgResDto> questionImages, List<QuestionItemResDto> questionItems) {
        long imageVoteCount = questionImages.stream()
                .mapToLong(QuestionImgResDto::getVoteNum)
                .sum();
        long itemVoteCount = questionItems.stream()
                .mapToLong(QuestionItemResDto::getVoteNum)
                .sum();
        return imageVoteCount + itemVoteCount;
    }

    private Double getVotePercent(Long voteNum, Long totalVoteNum) {
        double div = (double) voteNum / (double) totalVoteNum;
        return Math.round(div * 1000) / 10.0;
    }
}
