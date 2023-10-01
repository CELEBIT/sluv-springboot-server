package com.sluv.server.domain.closet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClosetListCountResDto {
    Long closetCount;
    List<ClosetResDto> closetList;

    public static ClosetListCountResDto of(Long closetCount, List<ClosetResDto> closetList){
        return ClosetListCountResDto.builder()
                .closetCount(closetCount)
                .closetList(closetList)
                .build();
    }
}
