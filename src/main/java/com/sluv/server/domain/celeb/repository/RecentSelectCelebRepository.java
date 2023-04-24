package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.RecentSelectCeleb;
import com.sluv.server.domain.celeb.repository.Impl.RecentSelectCelebRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecentSelectCelebRepository extends JpaRepository<RecentSelectCeleb, Long>, RecentSelectCelebRepositoryCustom {
}
