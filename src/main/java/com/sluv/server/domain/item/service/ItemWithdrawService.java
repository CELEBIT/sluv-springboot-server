package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.repository.RecentItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemWithdrawService {

    private final RecentItemRepository recentItemRepository;
    private final TempItemService tempItemService;

    @Transactional
    public void withdrawItemByUserId(Long userId) {
        recentItemRepository.deleteAllByUserId(userId);
        tempItemService.deleteAllTempItem(userId);
    }
}
