package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.dto.ItemEditReqDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemEditReq;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.repository.ItemEditReqRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemEditReqService {
    private final ItemEditReqRepository itemEditReqRepository;
    private final ItemRepository itemRepository;

    public void postItemEdit(User user, Long itemId, ItemEditReqDto dto) {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        itemEditReqRepository.save(
                ItemEditReq.toEntity(user, item, dto)
        );
    }
}
