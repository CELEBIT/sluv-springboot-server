package com.sluv.admin.item.service;

import com.sluv.admin.common.response.PaginationResponse;
import com.sluv.admin.common.service.ReportProcessingService;
import com.sluv.admin.item.dto.*;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.item.entity.ItemReport;
import com.sluv.domain.item.enums.ItemStatus;
import com.sluv.domain.item.service.ItemImgDomainService;
import com.sluv.domain.item.service.ItemLinkDomainService;
import com.sluv.domain.item.service.ItemReportDomainService;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.exception.InvalidReportStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemReportService {

    private final ItemReportDomainService itemDomainService;
    private final ItemImgDomainService itemImgDomainService;
    private final ItemLinkDomainService itemLinkDomainService;
    private final ReportProcessingService reportProcessingService;

    @Transactional(readOnly = true)
    public PaginationResponse<ItemReportInfoDto> getAllItemReport(Pageable pageable, ReportStatus reportStatus) {
        Page<ItemReport> itemReportPage = itemDomainService.getAllItemReport(pageable, reportStatus);
        List<ItemReportInfoDto> content = itemReportPage.getContent().stream()
                .map(ItemReportInfoDto::from)
                .toList();
        return PaginationResponse.create(itemReportPage, content);
    }

    @Transactional(readOnly = true)
    public ItemReportDetailDto getItemReportDetail(Long itemReportId) {
        ItemReport itemReport = itemDomainService.getItemReportDetail(itemReportId);
        List<ItemImgResDto> imgList = itemImgDomainService.findAllByItemId(itemReport.getItem().getId())
                .stream()
                .map(ItemImgResDto::from)
                .toList();

        List<ItemLinkResDto> linkList = itemLinkDomainService.findAllByItemId(itemReport.getItem().getId())
                .stream()
                .map(ItemLinkResDto::from)
                .toList();

        return ItemReportDetailDto.of(itemReport, imgList, linkList);
    }

    @Transactional
    public UpdateItemReportResDto updateItemReportStatus(Long itemReportId, ReportStatus reportStatus) {
        if (reportStatus == ReportStatus.WAITING) {
            throw new InvalidReportStatusException();
        }

        ItemReport itemReport = itemDomainService.findById(itemReportId);

        if (itemReport.getReportStatus() != ReportStatus.WAITING) {
            throw new InvalidReportStatusException();
        }

        User reportedUser = itemReport.getItem().getUser();
        User reporterUser = itemReport.getReporter();

        itemReport.changeItemReportStatus(reportStatus);

        if (reportStatus == ReportStatus.COMPLETE) {
            itemReport.getItem().changeItemStatus(ItemStatus.BLOCKED);
        }
        reportProcessingService.processReport(reportedUser, reporterUser, itemReport.getContent(), reportStatus);

        return UpdateItemReportResDto.of(itemReport.getReportStatus());
    }
}
