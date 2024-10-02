package com.sluv.domain.celeb.repository.Impl;

import com.sluv.domain.celeb.entity.Celeb;
import com.sluv.domain.celeb.entity.CelebCategory;
import com.sluv.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CelebRepositoryCustom {
    List<Celeb> findInterestedCeleb(User user);

    Page<Celeb> searchCeleb(String celebName, Pageable pageable);

    List<Celeb> findRecentCeleb(User user);

    List<Celeb> findTop10Celeb();

    List<Celeb> getCelebByCategory(CelebCategory category);

    List<Celeb> searchInterestedCelebByParent(String celebName);

    List<Celeb> searchInterestedCelebByChild(String celebName);

    List<Celeb> getCelebByContainKeyword(String keyword);
}