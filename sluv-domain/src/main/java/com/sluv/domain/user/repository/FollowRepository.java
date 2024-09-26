package com.sluv.domain.user.repository;

import com.sluv.domain.user.entity.Follow;
import com.sluv.domain.user.repository.impl.FollowRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {
}
