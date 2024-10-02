package com.sluv.domain.user.service;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserReport;
import com.sluv.domain.user.enums.UserReportReason;
import com.sluv.domain.user.repository.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReportDomainService {

    private final UserReportRepository userReportRepository;

    public boolean findExistence(User user, User target) {
        return userReportRepository.findExistence(user, target);
    }

    public void saveUserReport(User user, User target, UserReportReason reason, String content) {
        UserReport userReport = UserReport.toEntity(user, target, reason, content);
        userReportRepository.save(userReport);
    }

}