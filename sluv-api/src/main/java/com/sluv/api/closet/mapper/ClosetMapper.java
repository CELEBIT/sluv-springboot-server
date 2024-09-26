package com.sluv.api.closet.mapper;

import com.sluv.api.closet.dto.request.ClosetRequest;
import com.sluv.domain.closet.dto.ClosetDomainDto;

public class ClosetMapper {

    public static ClosetDomainDto toClosetDomainDto(ClosetRequest dto) {
        return ClosetDomainDto.of(dto.getName(), dto.getCoverImgUrl(), dto.getClosetStatus(), dto.getColorScheme());
    }

}
