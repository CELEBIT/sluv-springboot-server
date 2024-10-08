package com.sluv.api.question.dto;

import com.sluv.api.celeb.dto.response.CelebChipResponse;
import com.sluv.domain.question.entity.Question;
import com.sluv.domain.user.dto.UserInfoDto;
import com.sluv.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionGetDetailResDto {
    @Schema(description = "게시글의 Question 타입")
    private String qType;
    @Schema(description = "작성자 정보")
    private UserInfoDto user;
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 내용")
    private String content;
    @Schema(description = "게시글 이미지 Url 리스트")
    private List<QuestionImgResDto> imgList;
    @Schema(description = "게시글 아이템 리스트")
    private List<QuestionItemResDto> itemList;
    @Schema(description = "게시글 조회수")
    private Long searchNum;
    @Schema(description = "게시글 좋아요 수")
    private Long likeNum;
    @Schema(description = "게시글 댓글 수")
    private Long commentNum;
    @Schema(description = "게시글 작성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "현재 유저가 게시글에 좋아요를 남겼는지 판단")
    private Boolean hasLike;
    @Schema(description = "현재 유저가 작성한 게시글인지 판단")
    private Boolean hasMine;

    // Question Buy
    @Schema(description = "게시글 투표 종료시간")
    private LocalDateTime voteEndTime;
    @Schema(description = "게시글 투표 총 투표수")
    private Long totalVoteNum;
    @Schema(description = "현재 유저의 투표 상태, null -> 투표 안함.")
    private Long voteStatus;

    // Question Find
    @Schema(description = "게시글에 해당하는 Celeb")
    private CelebChipResponse celeb;
    @Schema(description = "게시글에 해당하는 NewCeleb")
    private CelebChipResponse newCeleb;

    // Question Recommend
    @Schema(description = "QuestionRecommend Category 리스트")
    private List<String> recommendCategoryList;

    public static QuestionGetDetailResDto of(Question question, String qType, User writer,
                                             List<QuestionImgResDto> imgList, List<QuestionItemResDto> itemList,
                                             Long likeNum, Long commentNum, Boolean hasLike, Boolean hasMine,
                                             CelebChipResponse celeb, CelebChipResponse newCeleb,
                                             LocalDateTime voteEndTime, Long totalVoteNum, Long voteStatus,
                                             List<String> recommendCategoryList) {

        return QuestionGetDetailResDto.builder()
                .qType(qType)
                .user(UserInfoDto.of(writer))
                .title(question.getTitle())
                .content(question.getContent())
                .imgList(imgList)
                .itemList(itemList)
                .searchNum(question.getSearchNum())
                .likeNum(likeNum)
                .commentNum(commentNum)
                .createdAt(question.getCreatedAt())
                .hasLike(hasLike)
                .hasMine(hasMine)
                .voteEndTime(voteEndTime)
                .totalVoteNum(totalVoteNum)
                .voteStatus(voteStatus)
                .celeb(celeb)
                .newCeleb(newCeleb)
                .recommendCategoryList(recommendCategoryList)
                .build();
    }

}
