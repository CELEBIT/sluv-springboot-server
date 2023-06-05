package com.sluv.server.domain.item.repository.impl;

import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.RecentItem;
import com.sluv.server.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    List<String> findTopPlace();
    List<Item> findSameCelebItem(Item item, boolean celebJudge);

    List<Item> findSameBrandItem(Item item, boolean brandJudge);

    Page<Item> getRecentItem(User user, Pageable pageable);
}
