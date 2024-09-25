package com.sluv.domain.user.repository;

import com.sluv.domain.user.entity.UserReport;
import com.sluv.domain.user.repository.impl.UserReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long>, UserReportRepositoryCustom {
}
