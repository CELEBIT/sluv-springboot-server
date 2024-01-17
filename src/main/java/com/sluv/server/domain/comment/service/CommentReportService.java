package com.sluv.server.domain.comment.service;

import com.sluv.server.domain.comment.dto.CommentReportPostReqDto;
import com.sluv.server.domain.comment.entity.Comment;
import com.sluv.server.domain.comment.entity.CommentReport;
import com.sluv.server.domain.comment.exception.CommentNotFoundException;
import com.sluv.server.domain.comment.exception.CommentReportDuplicateException;
import com.sluv.server.domain.comment.repository.CommentReportRepository;
import com.sluv.server.domain.comment.repository.CommentRepository;
import com.sluv.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentReportService {
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;

    /**
     * 댓글 신고
     */
    @Transactional
    public void postCommentReport(User user, Long commentId, CommentReportPostReqDto dto) {
        log.info("댓글 신고 - 사용자 : {}, 신고 댓글 : {}", user.getId(), commentId);
        Boolean reportStatus = commentReportRepository.existsByReporterIdAndCommentId(user.getId(), commentId);

        if (reportStatus) {
            throw new CommentReportDuplicateException();
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentReportRepository.save(
                CommentReport.toEntity(comment, user, dto)
        );
    }


}
