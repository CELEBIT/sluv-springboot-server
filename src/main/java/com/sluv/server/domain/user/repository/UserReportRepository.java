package com.sluv.server.domain.user.repository;

import com.sluv.server.domain.user.entity.UserReport;
import com.sluv.server.domain.user.repository.impl.UserReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport, Long>, UserReportRepositoryCustom {
}
