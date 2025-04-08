package com.sluv.domain.user.repository.impl;

import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserBlockRepositoryCustom {
    boolean getBlockStatus(User user, User targetUser);

    void deleteUserBlock(User user, User targetUser);

    Page<UserBlock> getUserBlockPage(Long userId, Pageable pageable);
}
