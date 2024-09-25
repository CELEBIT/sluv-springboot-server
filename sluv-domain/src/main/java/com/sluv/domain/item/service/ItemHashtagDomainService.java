package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.entity.hashtag.ItemHashtag;
import com.sluv.domain.item.repository.hashtag.ItemHashtagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemHashtagDomainService {

    private final ItemHashtagRepository itemHashtagRepository;

    @Transactional
    public void deleteAllByItemId(Long itemId) {
        itemHashtagRepository.deleteAllByItemId(itemId);
    }

    @Transactional(readOnly = true)
    public List<ItemHashtag> findAllByItemId(Long itemId) {
        return itemHashtagRepository.findAllByItemId(itemId);
    }

    @Transactional
    public void saveItemHashtag(Item newItem, Hashtag hashtag) {
        ItemHashtag itemHashtag = ItemHashtag.toEntity(newItem, hashtag);
        itemHashtagRepository.save(itemHashtag);
    }
}
