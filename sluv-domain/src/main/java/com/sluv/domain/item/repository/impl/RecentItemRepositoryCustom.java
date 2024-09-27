package com.sluv.domain.item.repository.impl;

import com.sluv.domain.item.entity.RecentItem;
import com.sluv.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecentItemRepositoryCustom {
    Page<RecentItem> getUserAllRecentItem(User user, Pageable pageable);
}
