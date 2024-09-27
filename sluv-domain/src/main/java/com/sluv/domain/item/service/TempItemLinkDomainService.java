package com.sluv.domain.item.service;

import com.sluv.domain.item.dto.ItemLinkDto;
import com.sluv.domain.item.entity.TempItem;
import com.sluv.domain.item.entity.TempItemLink;
import com.sluv.domain.item.repository.TempItemLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TempItemLinkDomainService {

    private final TempItemLinkRepository tempItemLinkRepository;

    public void deleteAllByTempItemId(Long tempItemId) {
        tempItemLinkRepository.deleteAllByTempItemId(tempItemId);
    }

    public void saveTempItemLink(TempItem saveTempItem, ItemLinkDto itemLinkDto) {
        TempItemLink tempItemLink = TempItemLink.toEntity(saveTempItem, itemLinkDto);
        tempItemLinkRepository.save(tempItemLink);
    }

    public List<TempItemLink> getTempItemLinks(List<TempItem> tempItems) {
        return tempItemLinkRepository.findAllByTempItems(tempItems);
    }
}
