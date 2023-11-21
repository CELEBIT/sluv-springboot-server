package com.sluv.server.domain.question.dto;

import com.sluv.server.domain.question.entity.Question;
import com.sluv.server.domain.question.entity.QuestionBuy;
import com.sluv.server.domain.question.entity.QuestionFind;
import com.sluv.server.domain.question.entity.QuestionHowabout;
import com.sluv.server.domain.question.entity.QuestionRecommend;
import com.sluv.server.domain.user.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static QuestionSimpleResDto of(Question question, Long likeNum, Long commentNum,
                                          List<QuestionImgSimpleResDto> imgList,
                                          List<QuestionImgSimpleResDto> itemImgList,
                                          List<String> categoryName) {

        String celebName = null;
        String qType = null;

        if (question instanceof QuestionBuy) {
            qType = "Buy";
        } else if (question instanceof QuestionFind questionFind) {
            qType = "Find";
            celebName = questionFind.getCeleb() != null
                    ? questionFind.getCeleb().getCelebNameKr()
                    : questionFind.getNewCeleb().getCelebName();
        } else if (question instanceof QuestionRecommend) {
            qType = "Recommend";
        } else if (question instanceof QuestionHowabout) {
            qType = "How";
        }

        return QuestionSimpleResDto.builder()
                .qType(qType)
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .user(UserInfoDto.of(question.getUser()))
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
