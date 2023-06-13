package com.sluv.server.domain.search.dto;

import com.sluv.server.domain.item.dto.ItemSimpleResDto;
import com.sluv.server.domain.question.dto.QuestionSimpleResDto;
import com.sluv.server.domain.user.dto.UserSearchInfoDto;
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
    List<ItemSimpleResDto> itemList;
    List<QuestionSimpleResDto> questionList;
    List<UserSearchInfoDto> userList;
}
