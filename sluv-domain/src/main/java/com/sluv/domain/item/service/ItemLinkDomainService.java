package com.sluv.domain.item.service;

import com.sluv.domain.item.dto.ItemLinkDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemLink;
import com.sluv.domain.item.repository.ItemLinkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemLinkDomainService {

    private final ItemLinkRepository itemLinkRepository;

    @Transactional
    public void deleteAllByItemId(Long itemId) {
        itemLinkRepository.deleteAllByItemId(itemId);
    }

    @Transactional(readOnly = true)
    public List<ItemLink> findByItemId(Long itemId) {
        return itemLinkRepository.findByItemId(itemId);
    }

    @Transactional
    public void saveItemLink(Item newItem, ItemLinkDto dto) {
        ItemLink itemLink = ItemLink.toEntity(newItem, dto);
        itemLinkRepository.save(itemLink);
    }
}
