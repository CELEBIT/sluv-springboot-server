package com.sluv.domain.user.repository.impl;

import com.sluv.domain.auth.enums.SnsType;
import com.sluv.domain.user.dto.UserReportStackDto;
import com.sluv.domain.user.dto.UserWithFollowerCountDto;
import com.sluv.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    Page<User> getSearchUser(List<Long> userIdList, Pageable pageable);

    Page<User> getAllFollower(Long userId, Pageable pageable);

    Page<User> getAllFollowing(Long userId, Pageable pageable);

    List<User> getHotSluver(Long celebId, List<Long> blockUserIds);

    List<User> getSearchUserIds(String word);

    long getNotDeleteUserCount();

    List<User> getDeletedUsersAfter7Days();

    Optional<User> findBySnsWithEmailOrNull(String email, SnsType snsType);

    Page<UserReportStackDto> getAllUserInfo(Pageable pageable);

    List<UserWithFollowerCountDto> getTop3HotUser();
}
