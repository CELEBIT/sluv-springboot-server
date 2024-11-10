package com.sluv.domain.item.service;

import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemEditReq;
import com.sluv.domain.item.enums.ItemEditReqReason;
import com.sluv.domain.item.repository.ItemEditReqRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemEditReqDomainService {

    private final ItemEditReqRepository itemEditReqRepository;

    public ItemEditReq saveItemEditReq(User user, Item item, ItemEditReqReason reason, String content) {
        ItemEditReq itemEditReq = ItemEditReq.toEntity(user, item, reason, content);
        return itemEditReqRepository.save(itemEditReq);
    }

    public ItemEditReq findItemEditReqByIdWithItem(Long editReqId) {
        return itemEditReqRepository.findByIdWithItem(editReqId);
    }
}
