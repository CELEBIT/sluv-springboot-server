package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemReport;
import com.sluv.domain.item.enums.ItemReportReason;
import com.sluv.domain.item.repository.ItemReportRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemReportDomainService {

    private final ItemReportRepository itemReportRepository;

    public boolean findExistence(User user, Item item) {
        return itemReportRepository.findExistence(user, item);
    }


    public void saveItemReport(Item item, User user, ItemReportReason reason, String content) {
        ItemReport itemReport = ItemReport.toEntity(item, user, reason, content);
        itemReportRepository.save(itemReport);
    }

}
