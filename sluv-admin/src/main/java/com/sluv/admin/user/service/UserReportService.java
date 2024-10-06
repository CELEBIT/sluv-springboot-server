package com.sluv.admin.user.service;

import com.sluv.admin.common.response.PaginationResponse;
import com.sluv.admin.common.service.ReportProcessingService;
import com.sluv.admin.user.dto.UpdateUserReportResDto;
import com.sluv.admin.user.dto.UserReportInfoDto;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.user.entity.User;
import com.sluv.domain.user.entity.UserReport;
import com.sluv.domain.user.exception.InvalidReportStatusException;
import com.sluv.domain.user.service.UserReportDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserReportService {

    private final UserReportDomainService userReportDomainService;
    private final ReportProcessingService reportProcessingService;

    @Transactional(readOnly = true)
    public PaginationResponse<UserReportInfoDto> getAllUserReport(Pageable pageable, ReportStatus reportStatus) {
        Page<UserReport> reportPage = userReportDomainService.getAllUserReport(pageable, reportStatus);
        List<UserReportInfoDto> content = reportPage.stream().map(UserReportInfoDto::from).toList();
        return PaginationResponse.create(reportPage, content);
    }

    public UpdateUserReportResDto updateUserReportStatus(Long userReportId, ReportStatus reportStatus) {
        if (reportStatus == ReportStatus.WAITING) {
            throw new InvalidReportStatusException();
        }

        UserReport userReport = userReportDomainService.findById(userReportId);

        if (userReport.getReportStatus() != ReportStatus.WAITING) {
            throw new InvalidReportStatusException();
        }

        User reportedUser = userReport.getReported();
        User reporterUser = userReport.getReporter();

        userReport.changeUserReportStatus(reportStatus);
        reportProcessingService.processReport(reportedUser, reporterUser, userReport.getContent(), reportStatus);

        return UpdateUserReportResDto.of(userReport.getReportStatus());
    }
}