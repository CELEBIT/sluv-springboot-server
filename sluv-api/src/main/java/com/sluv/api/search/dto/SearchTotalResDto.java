package com.sluv.api.search.dto;

import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.question.dto.QuestionSimpleResDto;
import com.sluv.domain.user.dto.UserSearchInfoDto;
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
public class SearchTotalResDto {
    @Schema(description = "조회된 item 리스트")
    List<ItemSimpleDto> itemList;
    @Schema(description = "조회된 Question 리스트")
    List<QuestionSimpleResDto> questionList;
    @Schema(description = "조회된 User 리스트")
    List<UserSearchInfoDto> userList;

    public static SearchTotalResDto of(List<ItemSimpleDto> itemList,
                                       List<QuestionSimpleResDto> questionList,
                                       List<UserSearchInfoDto> userList) {
        return SearchTotalResDto.builder()
                .itemList(itemList)
                .questionList(questionList)
                .userList(userList)
                .build();
    }
}
