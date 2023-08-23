package com.sluv.server.domain.question.dto;

import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.user.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionSimpleResDto {
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
    @Schema(description = "Question 좋아요 수")
    private Long likeNum;
    @Schema(description = "Question 조회수")
    private Long viewNum;
    @Schema(description = "Question 댓글 수")
    private Long commentNum;

    //찾아주세요
    @Schema(description = "QuestionFind 관련 Celeb 이름")
    private String celebName;

    //이 중에 뭐 살까
    @Schema(description = "QuestionBuy 게시글 이미지 URL 리스트")
    private List<QuestionImgSimpleResDto> imgList;
    @Schema(description = "QuestionBuy 게시글 아이템 대표 이미지 URL 리스트")
    private List<QuestionImgSimpleResDto> itemImgList;

    //추천해 줘
    @Schema(description = "QuestionRecommend 게시글 카테고리 리스트")
    private List<String> categoryName;
    public static QuestionSimpleResDto of(String qType, UserInfoDto userInfoDto,
                                          Long likeNum, Long commentNum,
                                          Question question,
                                          String celebName,
                                          List<QuestionImgSimpleResDto> imgList, List<QuestionImgSimpleResDto> itemImgList,
                                          List<String> categoryName){

        return QuestionSimpleResDto.builder()
                .qType(qType)
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .user(userInfoDto)
                .likeNum(likeNum)
                .viewNum(question.getSearchNum())
                .commentNum(commentNum)
                .celebName(celebName)
                .imgList(imgList)
                .itemImgList(itemImgList)
                .categoryName(categoryName)
                .build();
    }
}
