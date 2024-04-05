package com.sluv.server.domain.user.service;

import com.sluv.server.domain.user.repository.UserReportRepository;
import com.sluv.server.domain.user.repository.UserReportStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWithdrawService {
    private final UserReportRepository userReportRepository;
    private final UserReportStackRepository userReportStackRepository;

    public void withdrawUserByUserId(Long userId) {
        userReportRepository.withdrawByUserId(userId);
        userReportStackRepository.deleteAllByReportedId(userId);
    }
}
