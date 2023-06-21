package com.sluv.server.domain.closet.repository.impl;

import com.sluv.server.domain.closet.entity.Closet;
import com.sluv.server.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClosetRepositoryCustom {
    List<Closet> getRecentAddCloset(Item item);

    Page<Closet> getUserAllCloset(Long userId, Pageable pageable);

    Page<Closet> getUserAllPublicCloset(Long userId, Pageable pageable);
}
