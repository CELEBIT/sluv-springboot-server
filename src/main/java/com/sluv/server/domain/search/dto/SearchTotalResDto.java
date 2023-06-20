package com.sluv.server.domain.search.dto;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
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
public class SearchTotalResDto {
    @Schema(description = "조회된 item 리스트")
    List<ItemSimpleResDto> itemList;
    @Schema(description = "조회된 Question 리스트")
    List<QuestionSimpleResDto> questionList;
    @Schema(description = "조회된 User 리스트")
    List<UserSearchInfoDto> userList;
}
