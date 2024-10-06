package com.sluv.admin.comment.service;

import com.sluv.admin.comment.dto.CommentReportDetailResponse;
import com.sluv.admin.comment.dto.CommentReportInfoDto;
import com.sluv.admin.comment.dto.UpdateCommentReportResDto;
import com.sluv.admin.common.response.PaginationResponse;
import com.sluv.admin.common.service.ReportProcessingService;
import com.sluv.domain.comment.entity.CommentReport;
import com.sluv.domain.comment.enums.CommentStatus;
import com.sluv.domain.comment.service.CommentReportDomainService;
import com.sluv.domain.common.enums.ReportStatus;
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
@Transactional
public class CommentReportService {

    private final CommentReportDomainService commentReportDomainService;
    private final ReportProcessingService reportProcessingService;

    @Transactional(readOnly = true)
    public PaginationResponse<CommentReportInfoDto> getAllCommentReport(Pageable pageable, ReportStatus reportStatus) {
        Page<CommentReport> commentReportPage = commentReportDomainService.getAllCommentReport(pageable, reportStatus);
        List<CommentReportInfoDto> content = commentReportPage.getContent().stream()
                .map(commentReport -> CommentReportInfoDto.of(commentReport, commentReport.getComment()))
                .toList();
        return PaginationResponse.create(commentReportPage, content);
    }

    @Transactional(readOnly = true)
    public CommentReportDetailResponse getCommentReportDetail(Long commentReportId) {
        CommentReport commentReport = commentReportDomainService.getCommentReportDetail(commentReportId);
        return CommentReportDetailResponse.of(commentReport, commentReport.getComment());
    }

    public UpdateCommentReportResDto updateCommentReportStatus(Long commentReportId, ReportStatus reportStatus) {
        if (reportStatus == ReportStatus.WAITING) {
            throw new InvalidReportStatusException();
        }

        CommentReport commentReport = commentReportDomainService.findById(commentReportId);

        if (commentReport.getReportStatus() != ReportStatus.WAITING) {
            throw new InvalidReportStatusException();
        }

        User reportedUser = commentReport.getComment().getUser();
        User reporterUser = commentReport.getReporter();

        commentReport.changeCommentReportStatus(reportStatus);

        if (reportStatus == ReportStatus.COMPLETE) {
            commentReport.getComment().changeStatus(CommentStatus.BLOCKED);
        }
        reportProcessingService.processReport(reportedUser, reporterUser, commentReport.getContent(), reportStatus);

        return UpdateCommentReportResDto.of(commentReport.getReportStatus());
    }
}

