package com.sluv.api.item.dto;


import com.sluv.domain.item.dto.ItemSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEditReqResponseDto {

    private ItemSimpleDto itemSimpleDto;
    private String content;

    public static ItemEditReqResponseDto of(ItemSimpleDto itemSimpleDto, String content) {
        return ItemEditReqResponseDto.builder()
                .itemSimpleDto(itemSimpleDto)
                .content(content)
                .build();
    }
}
