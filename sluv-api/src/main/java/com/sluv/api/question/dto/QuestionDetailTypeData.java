package com.sluv.api.question.dto;

import com.sluv.api.celeb.dto.response.CelebChipResponse;
import java.time.LocalDateTime;
import java.util.List;

public record QuestionDetailTypeData(
        CelebChipResponse celeb,
        CelebChipResponse newCeleb,
        LocalDateTime voteEndTime,
        Long totalVoteNum,
        Long voteStatus,
        List<String> recommendCategories
) {

    public static QuestionDetailTypeData empty() {
        return new QuestionDetailTypeData(null, null, null, null, null, null);
    }

    public static QuestionDetailTypeData ofCeleb(CelebChipResponse celeb, CelebChipResponse newCeleb) {
        return new QuestionDetailTypeData(celeb, newCeleb, null, null, null, null);
    }

    public static QuestionDetailTypeData ofVote(LocalDateTime voteEndTime, Long totalVoteNum, Long voteStatus) {
        return new QuestionDetailTypeData(null, null, voteEndTime, totalVoteNum, voteStatus, null);
    }

    public static QuestionDetailTypeData ofRecommendCategories(List<String> recommendCategories) {
        return new QuestionDetailTypeData(null, null, null, null, null, recommendCategories);
    }
}
