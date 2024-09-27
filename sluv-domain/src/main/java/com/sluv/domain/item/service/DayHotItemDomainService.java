package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.DayHotItem;
import com.sluv.domain.item.repository.DayHotItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayHotItemDomainService {

    private final DayHotItemRepository dayHotItemRepository;

    public void deleteAllDayHotItem() {
        dayHotItemRepository.deleteAll();
    }

    public void save(DayHotItem dayHotItem) {
        dayHotItemRepository.save(dayHotItem);
    }

}
