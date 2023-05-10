package com.sluv.server.domain.celeb.repository.Impl;

import com.sluv.server.domain.celeb.entity.Celeb;
import com.sluv.server.domain.celeb.entity.CelebCategory;
import com.sluv.server.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CelebRepositoryCustom {
    List<Celeb> findInterestedCeleb(User user);
    Page<Celeb> searchCeleb(String celebName, Pageable pageable);
    List<Celeb> findRecentCeleb(User user);
    List<Celeb> findTop10Celeb();

    List<Celeb> getCelebByCategory(CelebCategory category);

    List<Celeb> searchInterestedCelebByParent(String celebName);

    List<Celeb> searchInterestedCelebByChild(String celebName);
}
