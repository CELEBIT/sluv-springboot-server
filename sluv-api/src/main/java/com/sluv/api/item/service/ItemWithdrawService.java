package com.sluv.api.item.service;

import com.sluv.domain.item.service.RecentItemDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemWithdrawService {

    private final RecentItemDomainService recentItemDomainService;
    private final TempItemService tempItemService;

    @Transactional
    public void withdrawItemByUserId(Long userId) {
        recentItemDomainService.deleteAllByUserId(userId);
        tempItemService.deleteAllTempItem(userId);
    }
}
