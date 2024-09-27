package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemLike;
import com.sluv.domain.item.repository.ItemLikeRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemLikeDomainService {

    private final ItemLikeRepository itemLikeRepository;

    public boolean existsByUserIdAndItemId(Long userId, Long itemId) {
        return itemLikeRepository.existsByUserIdAndItemId(userId, itemId);
    }

    public Integer countByItemId(Long itemId) {
        return itemLikeRepository.countByItemId(itemId);
    }

    public void saveItemLike(Item item, User user) {
        ItemLike itemLike = ItemLike.toEntity(item, user);
        itemLikeRepository.save(itemLike);
    }

    public void deleteByUserIdAndItemId(Long userId, Long itemId) {
        itemLikeRepository.deleteByUserIdAndItemId(userId, itemId);
    }
}
