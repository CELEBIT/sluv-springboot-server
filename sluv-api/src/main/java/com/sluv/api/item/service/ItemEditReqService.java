package com.sluv.api.item.service;

import com.sluv.api.alarm.service.ItemAlarmService;
import com.sluv.api.item.dto.ItemEditReqDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemEditReq;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemEditReqDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemEditReqService {
    private final ItemEditReqDomainService itemEditReqDomainService;
    private final ItemDomainService itemDomainService;
    private final ItemAlarmService itemAlarmService;
    private final UserDomainService userDomainService;

    public void postItemEdit(Long userId, Long itemId, ItemEditReqDto dto) {
        Item item = itemDomainService.findById(itemId);
        User user = userDomainService.findById(userId);
        ItemEditReq itemEditReq = itemEditReqDomainService.saveItemEditReq(user, item, dto.getReason(),
                dto.getContent());
        itemAlarmService.sendAlarmAboutItemEdit(item.getId(), itemEditReq.getId(), user);
    }
}
