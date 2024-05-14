package com.sluv.server.domain.user.repository;

import com.sluv.server.domain.user.entity.UserWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWithdrawRepository extends JpaRepository<UserWithdraw, Long> {
}
