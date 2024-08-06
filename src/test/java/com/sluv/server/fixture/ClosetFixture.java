package com.sluv.server.fixture;

import static com.sluv.server.domain.closet.enums.ClosetStatus.PRIVATE;
import static com.sluv.server.domain.closet.enums.ClosetStatus.PUBLIC;

import com.sluv.server.domain.closet.dto.ClosetReqDto;
import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.user.entity.User;

public class ClosetFixture {

    public static Closet 공개_옷장_생성(User user, String name) {
        ClosetReqDto closetReqDto = new ClosetReqDto(name, null, PUBLIC, null);
        return Closet.toEntity(user, closetReqDto);
    }

    public static Closet 비공개_옷장_생성(User user, String name) {
        ClosetReqDto closetReqDto = new ClosetReqDto(name, null, PRIVATE, null);
        return Closet.toEntity(user, closetReqDto);
    }
}
