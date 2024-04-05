package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.repository.ItemEditReqRepository;
import com.sluv.server.domain.item.repository.ItemLikeRepository;
import com.sluv.server.domain.item.repository.ItemReportRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.item.repository.RecentItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemWithdrawService {

    private final ItemRepository itemRepository;
    private final ItemLikeRepository itemLikeRepository;
    private final ItemEditReqRepository itemEditReqRepository;
    private final ItemReportRepository itemReportRepository;
    private final RecentItemRepository recentItemRepository;

    private final TempItemService tempItemService;

    @Transactional
    public void withdrawItemByUserId(Long userId) {
        itemRepository.withdrawByUserId(userId);
        itemLikeRepository.withdrawByUserId(userId);
        itemEditReqRepository.withdrawByUserId(userId);
        itemReportRepository.withdrawByUserId(userId);
        recentItemRepository.deleteAllByUserId(userId);
        tempItemService.deleteAllTempItem(userId);
    }
}
