package com.sluv.server.domain.item.service;

import com.sluv.server.domain.item.dto.ItemEditReqDto;
import com.sluv.server.domain.item.dto.ItemReportReqDto;
import com.sluv.server.domain.item.entity.Item;
import com.sluv.server.domain.item.entity.ItemReport;
import com.sluv.server.domain.item.exception.ItemNotFoundException;
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
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        itemReportRepository.save(
                ItemReport.builder()
                        .reporter(user)
                        .item(item)
                        .itemReportReason(dto.getReportReason())
                        .content(dto.getContent())
                        .reportStatus(ReportStatus.WAITING)
                        .build()
        );
    }
}
