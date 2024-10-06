package com.sluv.domain.user.repository;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserReportStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UserReportStackRepository extends JpaRepository<UserReportStack, Long> {
    void deleteAllByReportedId(Long userId);

    Long countByReportedAndCreatedAtAfter(User reportedUser, LocalDateTime createdAt);

}
