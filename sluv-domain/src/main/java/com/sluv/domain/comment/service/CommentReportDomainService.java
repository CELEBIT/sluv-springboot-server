package com.sluv.domain.comment.service;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.entity.CommentReport;
import com.sluv.domain.comment.enums.CommentReportReason;
import com.sluv.domain.comment.exception.CommentReportNotFoundException;
import com.sluv.domain.comment.repository.CommentReportRepository;
import com.sluv.domain.common.enums.ReportStatus;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentReportDomainService {

    private final CommentReportRepository commentReportRepository;

    public Boolean existsByReporterIdAndCommentId(Long reporterId, Long commentId) {
        return commentReportRepository.existsByReporterIdAndCommentId(reporterId, commentId);
    }

    public void saveCommentReport(Comment comment, User user, CommentReportReason reason, String content) {
        CommentReport commentReport = CommentReport.toEntity(comment, user, reason, content);
        commentReportRepository.save(commentReport);
    }

    public CommentReport getCommentReportDetail(Long commentReportId) {
        return commentReportRepository.getCommentReportDetail(commentReportId)
                .orElseThrow(CommentReportNotFoundException::new);
    }


    public Page<CommentReport> getAllCommentReport(Pageable pageable, ReportStatus reportStatus) {
        return commentReportRepository.getAllCommentReport(pageable, reportStatus);
    }

    public CommentReport findById(Long commentReportId) {
        return commentReportRepository.findById(commentReportId).orElseThrow(CommentReportNotFoundException::new);
    }
}
