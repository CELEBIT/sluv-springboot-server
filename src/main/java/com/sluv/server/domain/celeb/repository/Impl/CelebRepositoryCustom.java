package com.sluv.server.domain.celeb.repository.Impl;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.user.entity.User;

import java.util.List;

public interface CelebRepositoryCustom {

    List<Celeb> findInterestedCeleb(User user);
}
