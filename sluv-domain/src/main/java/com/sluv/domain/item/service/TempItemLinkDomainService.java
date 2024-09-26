package com.sluv.domain.item.service;

import com.sluv.domain.item.dto.ItemLinkDto;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemLink;
import com.sluv.domain.item.repository.TempItemLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TempItemLinkDomainService {

    private final TempItemLinkRepository tempItemLinkRepository;

    @Transactional
    public void deleteAllByTempItemId(Long tempItemId) {
        tempItemLinkRepository.deleteAllByTempItemId(tempItemId);
    }

    @Transactional
    public void saveTempItemLink(TempItem saveTempItem, ItemLinkDto itemLinkDto) {
        TempItemLink tempItemLink = TempItemLink.toEntity(saveTempItem, itemLinkDto);
        tempItemLinkRepository.save(tempItemLink);
    }

    @Transactional(readOnly = true)
    public List<TempItemLink> getTempItemLinks(List<TempItem> tempItems) {
        return tempItemLinkRepository.findAllByTempItems(tempItems);
    }
}
