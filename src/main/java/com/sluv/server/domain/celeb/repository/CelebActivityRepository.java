package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.CelebActivity;
import com.sluv.server.domain.celeb.repository.Impl.CelebActivityRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CelebActivityRepository extends JpaRepository<CelebActivity, Long>, CelebActivityRepositoryCustom {
}
