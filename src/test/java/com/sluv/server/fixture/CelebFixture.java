package com.sluv.server.fixture;

import static com.sluv.server.domain.celeb.enums.CelebStatus.ACTIVE;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.CelebCategory;
import com.sluv.server.domain.celeb.entity.NewCeleb;
import com.sluv.server.domain.celeb.enums.NewCelebStatus;

public class CelebFixture {

    public static Celeb 셀럽_생성(CelebCategory celebCategory, String krName, String enName, Celeb parent) {
        return Celeb.builder()
                .celebCategory(celebCategory)
                .celebNameKr(krName)
                .celebNameEn(enName)
                .parent(parent)
                .celebStatus(ACTIVE)
                .build();
    }

    public static CelebCategory 셀럽_카테고리_생성(String name, CelebCategory parent) {
        return CelebCategory.builder()
                .name(name)
                .parent(parent)
                .build();
    }

    public static NewCeleb 뉴셀럽_생성(String name) {
        return NewCeleb.builder()
                .celebName(name)
                .newCelebStatus(NewCelebStatus.ACTIVE)
                .build();
    }
}
