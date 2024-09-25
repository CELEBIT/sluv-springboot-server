package com.sluv.domain.comment.service;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.entity.CommentReport;
import com.sluv.domain.comment.enums.CommentReportReason;
import com.sluv.domain.comment.repository.CommentReportRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentReportDomainService {

    private final CommentReportRepository commentReportRepository;

    @Transactional(readOnly = true)
    public Boolean existsByReporterIdAndCommentId(Long reporterId, Long commentId) {
        return commentReportRepository.existsByReporterIdAndCommentId(reporterId, commentId);
    }

    @Transactional
    public void saveCommentReport(Comment comment, User user, CommentReportReason reason, String content) {
        CommentReport commentReport = CommentReport.toEntity(comment, user, reason, content);
        commentReportRepository.save(commentReport);
    }

}
