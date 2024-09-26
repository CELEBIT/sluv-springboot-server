package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.WeekHotItem;
import com.sluv.domain.item.repository.WeekHotItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WeekHotItemDomainService {

    private final WeekHotItemRepository weekHotItemRepository;

    @Transactional
    public void deleteAllWeekHotItem() {
        weekHotItemRepository.deleteAll();
    }

    @Transactional
    public void save(WeekHotItem weekHotItem) {
        weekHotItemRepository.save(weekHotItem);
    }

}
