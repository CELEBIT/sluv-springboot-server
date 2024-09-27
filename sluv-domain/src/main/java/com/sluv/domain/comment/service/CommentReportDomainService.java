package com.sluv.domain.comment.service;

import com.sluv.domain.comment.entity.Comment;
import com.sluv.domain.comment.entity.CommentReport;
import com.sluv.domain.comment.enums.CommentReportReason;
import com.sluv.domain.comment.repository.CommentReportRepository;
import com.sluv.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
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

}
