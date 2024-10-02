package com.sluv.api.item.service;

import com.sluv.api.item.dto.ItemEditReqDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemEditReq;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemEditReqDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.service.ItemAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemEditReqService {
    private final ItemEditReqDomainService itemEditReqDomainService;
    private final ItemDomainService itemDomainService;
    private final ItemAlarmService itemAlarmService;
    private final UserDomainService userDomainService;

    @Transactional
    public void postItemEdit(Long userId, Long itemId, ItemEditReqDto dto) {
        Item item = itemDomainService.findById(itemId);
        User user = userDomainService.findById(userId);
        ItemEditReq itemEditReq = itemEditReqDomainService.saveItemEditReq(user, item, dto.getReason(),
                dto.getContent());
        itemAlarmService.sendAlarmAboutItemEdit(user.getId(), item.getId(), itemEditReq.getId());
    }
}
