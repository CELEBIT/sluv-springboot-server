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

@Data
@AllArgsConstructor
@Builder
public class QuestionHomeResDto {
    @Schema(description = "Question 종류")
    private String qType;
    @Schema(description = "Question Id")
    private Long id;
    @Schema(description = "Question 제목")
    private String title;
    @Schema(description = "작성자 정보")
    private UserInfoDto user;
    @Schema(description = "Question 이미지")
    private List<QuestionImgSimpleResDto> imgList;

    public static QuestionHomeResDto of(Question question, List<QuestionImgSimpleResDto> imgList) {

        String qType = null;
        if (question instanceof QuestionBuy) {
            qType = "Buy";
        } else if (question instanceof QuestionFind) {
            qType = "Find";
        } else if (question instanceof QuestionRecommend) {
            qType = "Recommend";
        } else if (question instanceof QuestionHowabout) {
            qType = "How";
        }

        return QuestionHomeResDto.builder()
                .qType(qType)
                .id(question.getId())
                .title(question.getTitle())
                .user(UserInfoDto.of(question.getUser()))
                .imgList(imgList)
                .build();
    }

}
