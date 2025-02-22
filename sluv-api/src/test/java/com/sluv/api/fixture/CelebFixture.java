package com.sluv.api.fixture;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.celeb.entity.NewCeleb;
import com.sluv.domain.celeb.enums.NewCelebStatus;

import static com.sluv.domain.celeb.enums.CelebStatus.ACTIVE;

public class CelebFixture {

    public static Celeb 셀럽_생성() {
        return Celeb.builder()
                .celebCategory(new CelebCategory())
                .celebNameKr("krName")
                .celebNameEn("enName")
                .parent(new Celeb())
                .celebStatus(ACTIVE)
                .build();
    }

    public static CelebCategory 셀럽_카테고리_생성() {
        return CelebCategory.builder()
                .name("celebCategory")
                .parent(new CelebCategory())
                .build();
    }

    public static NewCeleb 뉴셀럽_생성(String name) {
        return NewCeleb.builder()
                .celebName(name)
                .newCelebStatus(NewCelebStatus.ACTIVE)
                .build();
    }
}
