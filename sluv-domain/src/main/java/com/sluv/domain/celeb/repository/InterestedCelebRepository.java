package com.sluv.domain.celeb.repository;

import com.sluv.domain.celeb.entity.InterestedCeleb;
import com.sluv.domain.celeb.repository.Impl.InterestedCelebRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestedCelebRepository extends JpaRepository<InterestedCeleb, Long>, InterestedCelebRepositoryCustom {
    void deleteAllByUserId(Long userId);
}
