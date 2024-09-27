package com.sluv.domain.alarm.dto;

import com.sluv.domain.question.dto.QuestionImgSimpleDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmImages {

    private List<QuestionImgSimpleDto> images;

    private String userImageUrl;

    public static AlarmImages of(List<QuestionImgSimpleDto> images, String userImageUrl) {
        return AlarmImages.builder()
                .images(images)
                .userImageUrl(userImageUrl)
                .build();
    }

}
