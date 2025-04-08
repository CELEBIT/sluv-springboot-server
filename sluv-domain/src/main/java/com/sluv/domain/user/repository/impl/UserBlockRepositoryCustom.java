package com.sluv.domain.user.repository.impl;

import com.sluv.domain.user.entity.User;

public interface UserBlockRepositoryCustom {
    boolean getBlockStatus(User user, User targetUser);

    void deleteUserBlock(User user, User targetUser);
}
