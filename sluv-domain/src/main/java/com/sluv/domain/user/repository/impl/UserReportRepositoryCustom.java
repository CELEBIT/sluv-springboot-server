package com.sluv.domain.user.repository.impl;

import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserReportRepositoryCustom {
    Boolean findExistence(User user, User target);

    void withdrawByUserId(Long userId);

    Page<UserReport> getAllUserReport(Pageable pageable, ReportStatus reportStatus);
}
