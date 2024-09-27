package com.sluv.domain.item.service;

import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemScrap;
import com.sluv.domain.item.repository.ItemScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemScrapDomainService {

    private final ItemScrapRepository itemScrapRepository;

    public void deleteAllByClosetId(Long closetId) {
        itemScrapRepository.deleteAllByClosetId(closetId);
    }

    public ItemScrap saveItemScrap(Item item, Closet closet) {
        ItemScrap itemScrap = ItemScrap.toEntity(item, closet);
        return itemScrapRepository.save(itemScrap);
    }

    public void deleteByClosetIdAndItemId(Long closetId, Long itemId) {
        itemScrapRepository.deleteByClosetIdAndItemId(closetId, itemId);
    }

    public ItemScrap findByClosetIdAndItemId(Long fromClosetId, Long itemId) {
        return itemScrapRepository.findByClosetIdAndItemId(fromClosetId, itemId);
    }

    public Long countByClosetId(Long closetId) {
        return itemScrapRepository.countByClosetId(closetId);
    }

    public Boolean getItemScrapStatus(Item item, List<Closet> closets) {
        return itemScrapRepository.getItemScrapStatus(item, closets);
    }

    public Integer countByItemId(Long itemId) {
        return itemScrapRepository.countByItemId(itemId);
    }
}
