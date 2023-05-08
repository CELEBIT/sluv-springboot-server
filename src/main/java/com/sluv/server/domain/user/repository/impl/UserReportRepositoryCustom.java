package com.sluv.server.domain.user.repository.impl;

import com.sluv.server.domain.user.entity.User;

public interface UserReportRepositoryCustom {
    Boolean findExistence(User user, User target);
}
