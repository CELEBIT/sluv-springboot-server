package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.dto.ItemReportReqDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemReport;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
import com.sluv.server.domain.item.exception.ItemReportDuplicateException;
import com.sluv.server.domain.item.repository.ItemReportRepository;
import com.sluv.server.domain.item.repository.ItemRepository;
import com.sluv.server.domain.user.entity.User;
import com.sluv.server.global.common.enums.ReportStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemReportService {
    private final ItemReportRepository itemReportRepository;
    private final ItemRepository itemRepository;


    public void postItemReport(User user, Long itemId, ItemReportReqDto dto) {
        // 신고대상 Item 검색.
        Item target = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        // 신고 중복여부 확인
        boolean existence = itemReportRepository.findExistence(user, target);

        // 중복 신고라면 Exception 발생
        if(existence){
            throw new ItemReportDuplicateException();
        }else {
        // 중복이 아니라면 신고 접수
            itemReportRepository.save(
                    ItemReport.toEntity(target, user, dto)
            );
        }
    }
}
