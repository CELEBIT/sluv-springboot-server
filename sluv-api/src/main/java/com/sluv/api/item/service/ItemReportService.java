package com.sluv.api.item.service;

import com.sluv.api.item.dto.ItemReportReqDto;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.exception.ItemReportDuplicateException;
import com.sluv.domain.item.service.ItemDomainService;
import com.sluv.domain.item.service.ItemReportDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemReportService {

    private final ItemReportDomainService itemReportDomainService;
    private final ItemDomainService itemDomainService;

    private final UserDomainService userDomainService;

    @Transactional
    public void postItemReport(Long userId, Long itemId, ItemReportReqDto dto) {
        User user = userDomainService.findById(userId);

        // 신고대상 Item 검색.
        Item item = itemDomainService.findById(itemId);

        // 신고 중복여부 확인
        boolean existence = itemReportDomainService.findExistence(user, item);

        // 중복 신고라면 Exception 발생
        if (existence) {
            throw new ItemReportDuplicateException();
        } else {
            // 중복이 아니라면 신고 접수
            itemReportDomainService.saveItemReport(item, user, dto.getReason(), dto.getContent());
        }
    }

}
