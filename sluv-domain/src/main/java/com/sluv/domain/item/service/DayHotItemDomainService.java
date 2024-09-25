package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.DayHotItem;
import com.sluv.domain.item.repository.DayHotItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DayHotItemDomainService {

    private final DayHotItemRepository dayHotItemRepository;

    @Transactional
    public void deleteAllDayHotItem() {
        dayHotItemRepository.deleteAll();
    }

    @Transactional
    public void save(DayHotItem dayHotItem) {
        dayHotItemRepository.save(dayHotItem);
    }

}
