package com.sluv.domain.user.repository;

import com.sluv.domain.user.entity.UserWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWithdrawRepository extends JpaRepository<UserWithdraw, Long> {
}
