package com.sluv.domain.closet.dto;

import com.sluv.domain.closet.entity.Closet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClosetCountDto {

    private Closet closet;
    private Long itemCount;

}
