package com.sluv.server.domain.user.repository.impl;

import com.sluv.server.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    Page<User> getSearchUser(List<Long> userIdList, Pageable pageable);
}
