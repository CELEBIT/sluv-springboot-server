package com.sluv.domain.celeb.repository;

import com.sluv.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.domain.celeb.repository.Impl.RecentSelectCelebRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSelectCelebRepository extends JpaRepository<RecentSelectCeleb, Long>,
        RecentSelectCelebRepositoryCustom {
    void deleteAllByUserId(Long id);

    void deleteByUserIdAndCelebId(Long userId, Long celebId);

    void deleteByUserIdAndNewCelebId(Long userId, Long newCelebId);
}
