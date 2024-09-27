package com.sluv.domain.item.repository;

import com.sluv.domain.item.entity.ItemReport;
import com.sluv.domain.item.repository.impl.ItemReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemReportRepository extends JpaRepository<ItemReport, Long>, ItemReportRepositoryCustom {

}
