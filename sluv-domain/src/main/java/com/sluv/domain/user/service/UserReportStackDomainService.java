package com.sluv.domain.user.service;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserReportStack;
import com.sluv.domain.user.repository.UserReportStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserReportStackDomainService {

    private final UserReportStackRepository userReportStackRepository;

    public void deleteAllByReportedId(Long userId) {
        userReportStackRepository.deleteAllByReportedId(userId);
    }

    public UserReportStack saveReportStack(User reportedUser) {
        UserReportStack userReportStack = UserReportStack.toEntity(reportedUser);
        return userReportStackRepository.save(userReportStack);
    }

    public long countByReportedAndCreatedAtAfter(User reportedUser, LocalDateTime oneMonthAgo) {
        return userReportStackRepository.countByReportedAndCreatedAtAfter(reportedUser, oneMonthAgo);
    }

}
