package com.sluv.domain.user.repository.impl;

import com.sluv.domain.user.entity.User;

public interface UserReportRepositoryCustom {
    Boolean findExistence(User user, User target);

    void withdrawByUserId(Long userId);
}
