package com.sluv.server.domain.celeb.repository;

import com.sluv.server.domain.celeb.entity.InterestedCeleb;
import com.sluv.server.domain.celeb.repository.Impl.CelebRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CelebRepository extends JpaRepository<InterestedCeleb, Long>, CelebRepositoryCustom {
}