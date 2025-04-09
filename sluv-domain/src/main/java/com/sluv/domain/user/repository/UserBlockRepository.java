package com.sluv.domain.user.repository;

import com.sluv.domain.user.entity.UserBlock;
import com.sluv.domain.user.repository.impl.UserBlockRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long>, UserBlockRepositoryCustom {
}
