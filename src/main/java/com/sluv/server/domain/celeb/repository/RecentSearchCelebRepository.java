package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.RecentSearchCeleb;
import com.sluv.server.domain.celeb.repository.Impl.RecentSearchCelebRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSearchCelebRepository extends JpaRepository<RecentSearchCeleb, Long>, RecentSearchCelebRepositoryCustom {
}
