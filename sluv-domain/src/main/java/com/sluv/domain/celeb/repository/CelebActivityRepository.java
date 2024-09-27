package com.sluv.domain.celeb.repository;

import com.sluv.domain.celeb.entity.CelebActivity;
import com.sluv.domain.celeb.repository.Impl.CelebActivityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelebActivityRepository extends JpaRepository<CelebActivity, Long>, CelebActivityRepositoryCustom {
}
