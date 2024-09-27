package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.LuxuryItem;
import com.sluv.domain.item.repository.LuxuryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LuxuryItemDomainService {

    private final LuxuryItemRepository luxuryItemRepository;


    public void deleteAllLuxuryItem() {
        luxuryItemRepository.deleteAll();
    }

    public void save(LuxuryItem entity) {
        luxuryItemRepository.save(entity);
    }
}
