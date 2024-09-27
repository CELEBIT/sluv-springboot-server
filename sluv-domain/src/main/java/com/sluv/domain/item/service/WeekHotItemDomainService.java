package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.WeekHotItem;
import com.sluv.domain.item.repository.WeekHotItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeekHotItemDomainService {

    private final WeekHotItemRepository weekHotItemRepository;

    public void deleteAllWeekHotItem() {
        weekHotItemRepository.deleteAll();
    }

    public void save(WeekHotItem weekHotItem) {
        weekHotItemRepository.save(weekHotItem);
    }

}
