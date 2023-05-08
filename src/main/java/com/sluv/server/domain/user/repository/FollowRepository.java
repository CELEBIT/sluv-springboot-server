package com.sluv.server.domain.user.repository;

import com.sluv.server.domain.user.entity.Follow;
import com.sluv.server.domain.user.repository.impl.FollowRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {
}
