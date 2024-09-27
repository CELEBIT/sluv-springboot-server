package com.sluv.api.fixture;

import static com.sluv.domain.notice.enums.NoticeStatus.ACTIVE;
import static com.sluv.domain.notice.enums.NoticeType.ETC;

import com.sluv.domain.notice.entity.Notice;

public class NoticeFixture {

    public static Notice 공지_생성(String title, String content) {
        return Notice.builder()
                .title(title)
                .content(content)
                .noticeType(ETC)
                .status(ACTIVE)
                .build();
    }
}
