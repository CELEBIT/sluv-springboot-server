package com.sluv.domain.user.repository.impl;

import com.sluv.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> getSearchUser(List<Long> userIdList, Pageable pageable);

    Page<User> getAllFollower(Long userId, Pageable pageable);

    Page<User> getAllFollowing(Long userId, Pageable pageable);

    List<User> getHotSluver(Long celebId);

    List<User> getSearchUserIds(String word);

    long getNotDeleteUserCount();

    List<User> getDeletedUsersAfter7Days();
}
