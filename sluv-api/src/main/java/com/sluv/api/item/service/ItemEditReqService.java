package com.sluv.api.item.service;

import com.sluv.api.item.dto.ItemEditReqDto;
import com.sluv.api.item.dto.ItemEditReqResponseDto;
import com.sluv.domain.item.dto.ItemSimpleDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemEditReq;
import com.sluv.domain.item.exception.ItemOwnerNotMatchException;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemEditReqDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import com.sluv.infra.alarm.service.ItemAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public ItemEditReqResponseDto getItemEdit(Long userId, Long editReqId) {
        ItemEditReq itemEditReq = itemEditReqDomainService.findItemEditReqByIdWithItem(editReqId);
        User itemUser = itemEditReq.getItem().getUser();
        if (!itemUser.getId().equals(userId)) {
            throw new ItemOwnerNotMatchException();
        }

        ItemSimpleDto itemSimpleDto = itemDomainService.getItemSimpleDto(null, List.of(itemEditReq.getItem())).get(0);
        return ItemEditReqResponseDto.of(itemSimpleDto, itemEditReq.getContent());
    }
}
