package com.sluv.domain.comment.repository.impl;

import com.sluv.domain.comment.entity.CommentReport;
import com.sluv.domain.common.enums.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentReportRepositoryCustom {
    Page<CommentReport> getAllCommentReport(Pageable pageable, ReportStatus reportStatus);

    Optional<CommentReport> getCommentReportDetail(Long commentReportId);
}