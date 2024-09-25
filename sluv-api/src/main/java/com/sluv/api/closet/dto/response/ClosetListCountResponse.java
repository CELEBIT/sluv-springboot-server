package com.sluv.api.closet.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClosetListCountResponse {
    Long closetCount;
    List<ClosetResponse> closetList;

    public static ClosetListCountResponse of(Long closetCount, List<ClosetResponse> closetList) {
        return ClosetListCountResponse.builder()
                .closetCount(closetCount)
                .closetList(closetList)
                .build();
    }

}
