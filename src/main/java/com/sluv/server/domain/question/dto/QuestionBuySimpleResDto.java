package com.sluv.server.domain.question.dto;

import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.entity.QuestionBuy;
import com.sluv.server.domain.question.entity.QuestionFind;
import com.sluv.server.domain.question.entity.QuestionHowabout;
import com.sluv.server.domain.question.entity.QuestionRecommend;
import com.sluv.server.domain.question.entity.QuestionVote;
import com.sluv.server.domain.user.dto.UserInfoDto;
import com.sluv.server.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionBuySimpleResDto {
    @Schema(description = "Question 종류")
    private String qType;
    @Schema(description = "Question Id")
    private Long id;
    @Schema(description = "Question 제목")
    private String title;
    @Schema(description = "Question 간략 내용")
    private String content;
    @Schema(description = "작성자 정보")
    private UserInfoDto user;
    @Schema(description = "Question 투표 수")
    private Long voteNum;

    //이 중에 뭐 살까
    @Schema(description = "QuestionBuy 게시글 이미지 URL 리스트")
    private List<QuestionImgResDto> imgList;
    @Schema(description = "QuestionBuy 게시글 아이템 대표 이미지 URL 리스트")
    private List<QuestionItemResDto> itemImgList;
    @Schema(description = "QuestionBuy 게시글 투표 마감날짜")
    private LocalDateTime voteEndTime;
    @Schema(description = "Question 투표 상태")
    private Boolean voteStatus;
    @Schema(description = "Question 투표 번호")
    private Long selectedVoteNum;
    @Schema(description = "Question 게시글이 자신의 것인지")
    private Boolean hasMine;


    public static QuestionBuySimpleResDto of(User user, Question question, User writer, Long voteNum,
                                             List<QuestionImgResDto> imgList,
                                             List<QuestionItemResDto> itemImgList,
                                             LocalDateTime voteEndTime, QuestionVote questionVote) {

        String qType = null;

        if (question instanceof QuestionBuy) {
            qType = "Buy";
        } else if (question instanceof QuestionFind questionFind) {
            qType = "Find";
        } else if (question instanceof QuestionRecommend) {
            qType = "Recommend";
        } else if (question instanceof QuestionHowabout) {
            qType = "How";
        }

        return QuestionBuySimpleResDto.builder()
                .qType(qType)
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .user(UserInfoDto.of(writer))
                .voteNum(voteNum)
                .imgList(imgList)
                .itemImgList(itemImgList)
                .voteEndTime(voteEndTime)
                .voteStatus(questionVote != null)
                .selectedVoteNum(questionVote != null ? questionVote.getVoteSortOrder() : null)
                .hasMine(Objects.equals(question.getUser().getId(), user.getId()))
                .build();
    }
}
