package com.sluv.domain.item.service;

import com.sluv.domain.closet.entity.Closet;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemScrap;
import com.sluv.domain.item.repository.ItemScrapRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemScrapDomainService {

    private final ItemScrapRepository itemScrapRepository;

    @Transactional
    public void deleteAllByClosetId(Long closetId) {
        itemScrapRepository.deleteAllByClosetId(closetId);
    }

    @Transactional
    public ItemScrap saveItemScrap(Item item, Closet closet) {
        ItemScrap itemScrap = ItemScrap.toEntity(item, closet);
        return itemScrapRepository.save(itemScrap);
    }

    @Transactional
    public void deleteByClosetIdAndItemId(Long closetId, Long itemId) {
        itemScrapRepository.deleteByClosetIdAndItemId(closetId, itemId);
    }

    @Transactional(readOnly = true)
    public ItemScrap findByClosetIdAndItemId(Long fromClosetId, Long itemId) {
        return itemScrapRepository.findByClosetIdAndItemId(fromClosetId, itemId);
    }

    @Transactional(readOnly = true)
    public Long countByClosetId(Long closetId) {
        return itemScrapRepository.countByClosetId(closetId);
    }

    @Transactional(readOnly = true)
    public Boolean getItemScrapStatus(Item item, List<Closet> closets) {
        return itemScrapRepository.getItemScrapStatus(item, closets);
    }

    @Transactional(readOnly = true)
    public Integer countByItemId(Long itemId) {
        return itemScrapRepository.countByItemId(itemId);
    }
}
