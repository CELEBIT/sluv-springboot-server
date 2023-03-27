package com.sluv.server.domain.celeb.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sluv.server.domain.celeb.entity.QCeleb.celeb;
import static com.sluv.server.domain.celeb.entity.QInterestedCeleb.interestedCeleb;

@RequiredArgsConstructor
public class CelebRepositoryImpl implements CelebRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Celeb> findInterestedCeleb(User user) {

        return jpaQueryFactory.select(celeb)
                .from(interestedCeleb)
                .where(interestedCeleb.user.eq(user))
                .fetch();
    }
}
