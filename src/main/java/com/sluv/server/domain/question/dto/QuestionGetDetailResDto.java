package com.sluv.server.domain.question.dto;

import com.sluv.server.domain.item.dto.ItemSameResDto;
import com.sluv.server.domain.user.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionGetDetailResDto {
    private String qType;
    private UserInfoDto user;
    private String title;
    private String content;
    private List<String> imgList;
    private List<ItemSameResDto> itemList;
    private Long searchNum;
    private Long likeNum;
    private Long commentNum;
    private LocalDateTime createdAt;

    private Boolean hasLike;
    private Boolean hasMine;



}
