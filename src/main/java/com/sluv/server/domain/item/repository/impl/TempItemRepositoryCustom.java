package com.sluv.server.domain.item.repository.impl;

import com.sluv.server.domain.item.entity.TempItem;
import com.sluv.server.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TempItemRepositoryCustom {
    Page<TempItem> getTempItemList(User user, Pageable pageable);

    List<TempItem> findAllExceptLast(User user);
}
