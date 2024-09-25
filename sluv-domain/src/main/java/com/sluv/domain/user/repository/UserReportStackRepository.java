package com.sluv.domain.user.repository;

import com.sluv.domain.user.entity.UserReportStack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportStackRepository extends JpaRepository<UserReportStack, Long> {
    void deleteAllByReportedId(Long userId);
}
