package com.sluv.api.fixture;

import static com.sluv.domain.closet.enums.ClosetStatus.PRIVATE;
import static com.sluv.domain.closet.enums.ClosetStatus.PUBLIC;

import com.sluv.api.closet.dto.request.ClosetRequest;
import com.sluv.api.closet.mapper.ClosetMapper;
import com.sluv.domain.closet.dto.ClosetDomainDto;
import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.user.entity.User;

public class ClosetFixture {

    public static Closet 공개_옷장_생성(User user, String name) {
        ClosetRequest closetRequest = new ClosetRequest(name, null, PUBLIC, null);
        ClosetDomainDto closetDomainDto = ClosetMapper.toClosetDomainDto(closetRequest);
        return Closet.toEntity(user, closetDomainDto);
    }

    public static Closet 비공개_옷장_생성(User user, String name) {
        ClosetRequest closetRequest = new ClosetRequest(name, null, PRIVATE, null);
        ClosetDomainDto closetDomainDto = ClosetMapper.toClosetDomainDto(closetRequest);
        return Closet.toEntity(user, closetDomainDto);
    }
}
