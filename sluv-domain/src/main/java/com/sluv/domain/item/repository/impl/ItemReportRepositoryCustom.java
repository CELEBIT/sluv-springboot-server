package com.sluv.domain.item.repository.impl;

import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.item.entity.Item;
import com.sluv.domain.item.entity.ItemReport;
import com.sluv.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemReportRepositoryCustom {
    Boolean findExistence(User user, Item target);

    Page<ItemReport> getAllItemReport(Pageable pageable, ReportStatus reportStatus);

    ItemReport getItemReportDetail(Long itemReportId);
}
