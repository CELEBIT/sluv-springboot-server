package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.RecentItem;
import com.sluv.domain.item.repository.RecentItemRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecentItemDomainService {

    private final RecentItemRepository recentItemRepository;

    @Transactional
    public void deleteAllByUserId(Long userId) {
        recentItemRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void saveRecentItem(Item item, User user) {
        RecentItem recentItem = RecentItem.of(item, user);
        recentItemRepository.save(recentItem);
    }
}
