package com.sluv.server.domain.closet.dto;

import com.sluv.server.domain.closet.enums.ClosetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClosetPostReqDto {
    private String name;
    private String coverImgUrl;
    private ClosetStatus closetStatus;
    private String color;

}
