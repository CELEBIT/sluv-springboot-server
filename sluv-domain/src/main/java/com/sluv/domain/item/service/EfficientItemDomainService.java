package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.EfficientItem;
import com.sluv.domain.item.repository.EfficientItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EfficientItemDomainService {

    private final EfficientItemRepository efficientItemRepository;


    @Transactional
    public void deleteAllEfficientItem() {
        efficientItemRepository.deleteAll();
    }

    @Transactional
    public void save(EfficientItem efficientItem) {
        efficientItemRepository.save(efficientItem);
    }

}
