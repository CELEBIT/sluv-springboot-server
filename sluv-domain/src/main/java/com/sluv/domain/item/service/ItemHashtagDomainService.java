package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.hashtag.Hashtag;
import com.sluv.domain.item.entity.hashtag.ItemHashtag;
import com.sluv.domain.item.repository.hashtag.ItemHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemHashtagDomainService {

    private final ItemHashtagRepository itemHashtagRepository;

    public void deleteAllByItemId(Long itemId) {
        itemHashtagRepository.deleteAllByItemId(itemId);
    }

    public List<ItemHashtag> findAllByItemId(Long itemId) {
        return itemHashtagRepository.findAllByItemId(itemId);
    }

    public void saveItemHashtag(Item newItem, Hashtag hashtag) {
        ItemHashtag itemHashtag = ItemHashtag.toEntity(newItem, hashtag);
        itemHashtagRepository.save(itemHashtag);
    }
}
